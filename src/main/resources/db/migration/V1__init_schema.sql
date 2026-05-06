-- =============================================================
--  ClinicFlow — V1__init_schema.sql
--  Flyway migration : V1
--  Database        : PostgreSQL 14+
--  Description     : Creates all tables, constraints, indexes,
--                    triggers for ClinicFlow from scratch.
--
--  Location in project:
--  src/main/resources/db/migration/V1__init_schema.sql
-- =============================================================


-- ----------------------------------------------------------------
-- Helper function: auto-update updated_at on every UPDATE
-- Used by: staff, appointment
-- ----------------------------------------------------------------
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- ================================================================
-- 1. ROLE
--    Lookup table. Seeded in V2.
--    Values: ADMIN | NURSE | DOCTOR
-- ================================================================
CREATE TABLE role (
    id          BIGSERIAL     PRIMARY KEY,
    name        VARCHAR(20)   NOT NULL UNIQUE,
    description VARCHAR(100),
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);


-- ================================================================
-- 2. STAFF
--    Single table for all user types (Admin, Nurse, Doctor).
--    Discriminated by role_id FK.
--
--    official_role  : human job title  e.g. "Senior Consultant"
--    specialization : clinical field   e.g. "Cardiology" (doctors only)
--    must_reset_password : set TRUE on account creation and after
--                          admin-triggered reset; forces password
--                          change before any other action.
-- ================================================================
CREATE TABLE staff (
    id                  BIGSERIAL    PRIMARY KEY,
    employee_id         VARCHAR(20)  NOT NULL UNIQUE,
    full_name           VARCHAR(100) NOT NULL,
    email               VARCHAR(150) NOT NULL UNIQUE,
    password_hash       VARCHAR(255) NOT NULL,
    role_id             BIGINT       NOT NULL REFERENCES role (id),
    phone               VARCHAR(15),
    official_role       VARCHAR(100),
    specialization      VARCHAR(100),
    is_active           BOOLEAN      NOT NULL DEFAULT TRUE,
    must_reset_password BOOLEAN      NOT NULL DEFAULT TRUE,
    last_login_at       TIMESTAMPTZ,
    created_by          BIGINT       REFERENCES staff (id),   -- self-ref; NULL for seed admin
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TRIGGER trg_staff_updated_at
    BEFORE UPDATE ON staff
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE INDEX idx_staff_role_id   ON staff (role_id);
CREATE INDEX idx_staff_is_active ON staff (is_active);


-- ================================================================
-- 3. PATIENT
--    No login credentials. Registered exclusively by clinic staff.
--    mobile is the unique deduplication key.
-- ================================================================
CREATE TABLE patient (
    id            BIGSERIAL   PRIMARY KEY,
    full_name     VARCHAR(100) NOT NULL,
    mobile        VARCHAR(15)  NOT NULL UNIQUE,
    gender        VARCHAR(10)  NOT NULL
                      CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    date_of_birth DATE,
    blood_group   VARCHAR(5),
    registered_by BIGINT       NOT NULL REFERENCES staff (id),
    registered_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_patient_mobile ON patient (mobile);


-- ================================================================
-- 4. DOCTOR_SCHEDULE
--    Timeless weekly template — NOT a calendar.
--    One row per (doctor, weekday). Admin manages.
--    Specific date overrides go into leave_exception.
--
--    UNIQUE (doctor_id, day_of_week) guarantees exactly one slot
--    per doctor per weekday, making the availability join safe.
-- ================================================================
CREATE TABLE doctor_schedule (
    id               BIGSERIAL   PRIMARY KEY,
    doctor_id        BIGINT      NOT NULL REFERENCES staff (id),
    day_of_week      VARCHAR(3)  NOT NULL
                         CHECK (day_of_week IN ('MON','TUE','WED','THU','FRI','SAT','SUN')),
    start_time       TIME        NOT NULL,
    end_time         TIME        NOT NULL,
    max_appointments INT         NOT NULL DEFAULT 20
                         CHECK (max_appointments > 0),
    is_active        BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_doctor_day   UNIQUE (doctor_id, day_of_week),
    CONSTRAINT chk_schedule_times CHECK (end_time > start_time)
);


-- ================================================================
-- 5. LEAVE_EXCEPTION
--    Overrides one specific calendar date for a schedule slot.
--    FK to doctor_schedule (not staff) so a single weekday slot
--    can be targeted independently of other slots on the same day.
--
--    Derivation at query time:
--      1. date → day_of_week (Java: LocalDate.getDayOfWeek())
--      2. SELECT id FROM doctor_schedule
--            WHERE doctor_id=? AND day_of_week=?
--      3. SELECT FROM leave_exception
--            WHERE doctor_schedule_id=? AND exception_date=?
-- ================================================================
CREATE TABLE leave_exception (
    id                 BIGSERIAL    PRIMARY KEY,
    doctor_schedule_id BIGINT       NOT NULL REFERENCES doctor_schedule (id) ON DELETE CASCADE,
    exception_date     DATE         NOT NULL,
    reason             VARCHAR(200),
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_leave_date UNIQUE (doctor_schedule_id, exception_date)
);


-- ================================================================
-- 6. APPOINTMENT
--    Central operational entity. Every patient visit starts here.
--
--    queue_number: sequential per (doctor, date).
--    Assigned atomically in the service layer with SELECT MAX+1
--    FOR UPDATE. The UNIQUE constraint is the DB-level safety net.
--
--    Status lifecycle:
--      WAITING → IN_PROGRESS  (doctor starts consultation)
--      WAITING → CANCELLED    (nurse/admin cancels)
--      IN_PROGRESS → COMPLETED (doctor submits prescription)
-- ================================================================
CREATE TABLE appointment (
    id               BIGSERIAL    PRIMARY KEY,
    patient_id       BIGINT       NOT NULL REFERENCES patient (id),
    doctor_id        BIGINT       NOT NULL REFERENCES staff (id),
    booked_by        BIGINT       NOT NULL REFERENCES staff (id),
    appointment_date DATE         NOT NULL,
    queue_number     INT          NOT NULL CHECK (queue_number > 0),
    status           VARCHAR(15)  NOT NULL DEFAULT 'WAITING'
                         CHECK (status IN ('WAITING','IN_PROGRESS','COMPLETED','CANCELLED')),
    notes            VARCHAR(500),
    cancelled_by     BIGINT       REFERENCES staff (id),
    cancelled_at     TIMESTAMPTZ,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_queue_slot UNIQUE (doctor_id, appointment_date, queue_number)
);

CREATE TRIGGER trg_appointment_updated_at
    BEFORE UPDATE ON appointment
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- Composite index for live queue polling (nurse dashboard, doctor dashboard)
CREATE INDEX idx_appt_queue   ON appointment (doctor_id, appointment_date, status);
-- Index for patient history lookup
CREATE INDEX idx_appt_patient ON appointment (patient_id);


-- ================================================================
-- 7. CONSULTATION
--    Created when doctor clicks "Start Consultation".
--    1-to-1 with appointment (enforced by UNIQUE on appointment_id).
--    Locked (is_locked = TRUE) after prescription is submitted.
--    Any PATCH on a locked consultation must return HTTP 409.
-- ================================================================
CREATE TABLE consultation (
    id             BIGSERIAL    PRIMARY KEY,
    appointment_id BIGINT       NOT NULL UNIQUE REFERENCES appointment (id),
    doctor_id      BIGINT       NOT NULL REFERENCES staff (id),
    clinical_notes TEXT,
    diagnosis      VARCHAR(500),
    is_locked      BOOLEAN      NOT NULL DEFAULT FALSE,
    started_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    completed_at   TIMESTAMPTZ,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- Index for doctor's own history view (GET /api/doctors/me/queue)
CREATE INDEX idx_consult_doctor ON consultation (doctor_id, started_at DESC);


-- ================================================================
-- 8. PRESCRIPTION
--    1-to-1 with consultation (enforced by UNIQUE on consultation_id).
--    Submitting a prescription must atomically:
--      1. Insert prescription row
--      2. Insert all prescription_medicine rows
--      3. Set consultation.is_locked    = TRUE
--      4. Set prescription.is_locked    = TRUE
--      5. Set appointment.status        = 'COMPLETED'
--      6. Set consultation.completed_at = NOW()
-- ================================================================
CREATE TABLE prescription (
    id                   BIGSERIAL    PRIMARY KEY,
    consultation_id      BIGINT       NOT NULL UNIQUE REFERENCES consultation (id),
    general_instructions TEXT,
    follow_up_notes      VARCHAR(500),
    follow_up_date       DATE,
    is_locked            BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);


-- ================================================================
-- 9. PRESCRIPTION_MEDICINE
--    Line items on a prescription.
--    Medicine catalog merged here (no separate medicine table).
--    medicine_name, medicine_category, medicine_unit are free-text
--    fields — no FK to a catalog, simpler for doctors to fill.
-- ================================================================
CREATE TABLE prescription_medicine (
    id                BIGSERIAL    PRIMARY KEY,
    prescription_id   BIGINT       NOT NULL REFERENCES prescription (id) ON DELETE CASCADE,
    medicine_name     VARCHAR(150) NOT NULL,
    medicine_category VARCHAR(80),
    medicine_unit     VARCHAR(20),
    dosage            VARCHAR(50)  NOT NULL,
    frequency         VARCHAR(50)  NOT NULL,
    duration_days     INT          NOT NULL CHECK (duration_days > 0),
    instructions      VARCHAR(200),
    sort_order        INT          NOT NULL DEFAULT 0
);

CREATE INDEX idx_rxmed_prescription ON prescription_medicine (prescription_id);
