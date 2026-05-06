-- =============================================================
--  ClinicFlow — V2__seed_data.sql
--  Flyway migration : V2
--  Database        : PostgreSQL 14+
--  Description     : Seeds roles, staff accounts, patients,
--                    doctor schedules, leave exceptions,
--                    appointments, consultations, prescriptions.
--
--  Location in project:
--  src/main/resources/db/migration/V2__seed_data.sql
--
--  Password note:
--  All password_hash values below are BCrypt hashes of "Welcome@1"
--  Hash: $2a$12$7Kq3sL9mNpXvR2wYtBcO8eGjFhIkMnPqRsUvWxYzAbCdEfGhIjKl
--  On first login, must_reset_password=TRUE forces every user
--  to change their password immediately.
-- =============================================================


-- ================================================================
-- 1. ROLES
--    Fixed set — never changes at runtime.
-- ================================================================
INSERT INTO role (name, description) VALUES
    ('ADMIN',  'Clinic operator — full oversight and staff management'),
    ('NURSE',  'Front desk — patient registration, booking, queue monitor'),
    ('DOCTOR', 'Consulting staff — manages own queue and records prescriptions');


-- ================================================================
-- 2. STAFF
--    Seed accounts:
--      1 Admin, 2 Nurses, 3 Doctors
--
--    employee_id convention:
--      AD-001        → Admin
--      NR-001, NR-002 → Nurses
--      DR-001 … DR-003 → Doctors
--
--    created_by is NULL for all seed accounts (no creator in DB).
--    must_reset_password = TRUE — all must reset on first login.
-- ================================================================
INSERT INTO staff (
    employee_id, full_name, email, password_hash,
    role_id, phone, official_role, specialization,
    is_active, must_reset_password, created_by
) VALUES

-- Admin
(
    'AD-001', 'Priya Nair', 'priya.nair@clinicflow.com',
    '$2a$12$7Kq3sL9mNpXvR2wYtBcO8eGjFhIkMnPqRsUvWxYzAbCdEfGhIjKl',
    (SELECT id FROM role WHERE name = 'ADMIN'),
    '9800000001', 'Clinic Administrator', NULL,
    TRUE, TRUE, NULL
),

-- Nurses
(
    'NR-001', 'Sunita Patil', 'sunita.patil@clinicflow.com',
    '$2a$12$7Kq3sL9mNpXvR2wYtBcO8eGjFhIkMnPqRsUvWxYzAbCdEfGhIjKl',
    (SELECT id FROM role WHERE name = 'NURSE'),
    '9800000002', 'Senior Nurse', NULL,
    TRUE, TRUE, NULL
),
(
    'NR-002', 'Meena Joshi', 'meena.joshi@clinicflow.com',
    '$2a$12$7Kq3sL9mNpXvR2wYtBcO8eGjFhIkMnPqRsUvWxYzAbCdEfGhIjKl',
    (SELECT id FROM role WHERE name = 'NURSE'),
    '9800000003', 'Staff Nurse', NULL,
    TRUE, TRUE, NULL
),

-- Doctors
(
    'DR-001', 'Dr. Arjun Sharma', 'arjun.sharma@clinicflow.com',
    '$2a$12$7Kq3sL9mNpXvR2wYtBcO8eGjFhIkMnPqRsUvWxYzAbCdEfGhIjKl',
    (SELECT id FROM role WHERE name = 'DOCTOR'),
    '9800000004', 'Senior Consultant', 'General Medicine',
    TRUE, TRUE, NULL
),
(
    'DR-002', 'Dr. Kavya Reddy', 'kavya.reddy@clinicflow.com',
    '$2a$12$7Kq3sL9mNpXvR2wYtBcO8eGjFhIkMnPqRsUvWxYzAbCdEfGhIjKl',
    (SELECT id FROM role WHERE name = 'DOCTOR'),
    '9800000005', 'Consultant', 'Cardiology',
    TRUE, TRUE, NULL
),
(
    'DR-003', 'Dr. Rahul Mehta', 'rahul.mehta@clinicflow.com',
    '$2a$12$7Kq3sL9mNpXvR2wYtBcO8eGjFhIkMnPqRsUvWxYzAbCdEfGhIjKl',
    (SELECT id FROM role WHERE name = 'DOCTOR'),
    '9800000006', 'Junior Consultant', 'Orthopedics',
    TRUE, TRUE, NULL
);


-- ================================================================
-- 3. PATIENTS
--    10 sample patients registered by NR-001 (Sunita Patil).
-- ================================================================
INSERT INTO patient (full_name, mobile, gender, date_of_birth, blood_group, registered_by)
VALUES
    ('Ramesh Kumar',    '9100000001', 'MALE',   '1980-03-15', 'B+',  (SELECT id FROM staff WHERE employee_id = 'NR-001')),
    ('Anjali Singh',    '9100000002', 'FEMALE', '1992-07-22', 'A+',  (SELECT id FROM staff WHERE employee_id = 'NR-001')),
    ('Mohan Das',       '9100000003', 'MALE',   '1975-11-08', 'O+',  (SELECT id FROM staff WHERE employee_id = 'NR-001')),
    ('Pooja Verma',     '9100000004', 'FEMALE', '1998-01-30', 'AB+', (SELECT id FROM staff WHERE employee_id = 'NR-001')),
    ('Suresh Yadav',    '9100000005', 'MALE',   '1965-06-12', 'B-',  (SELECT id FROM staff WHERE employee_id = 'NR-001')),
    ('Lakshmi Iyer',    '9100000006', 'FEMALE', '1988-09-05', 'O-',  (SELECT id FROM staff WHERE employee_id = 'NR-001')),
    ('Vikram Patel',    '9100000007', 'MALE',   '2000-12-18', 'A-',  (SELECT id FROM staff WHERE employee_id = 'NR-001')),
    ('Deepa Nair',      '9100000008', 'FEMALE', '1978-04-27', 'B+',  (SELECT id FROM staff WHERE employee_id = 'NR-001')),
    ('Arun Pillai',     '9100000009', 'MALE',   '1990-08-14', 'AB-', (SELECT id FROM staff WHERE employee_id = 'NR-001')),
    ('Sneha Kulkarni',  '9100000010', 'FEMALE', '1985-02-03', 'O+',  (SELECT id FROM staff WHERE employee_id = 'NR-001'));


-- ================================================================
-- 4. DOCTOR SCHEDULE
--    Weekly templates (timeless — no dates here).
--
--    DR-001 (Arjun Sharma)  : Mon Wed Fri — General Medicine
--    DR-002 (Kavya Reddy)   : Mon Tue Thu — Cardiology
--    DR-003 (Rahul Mehta)   : Tue Wed Sat — Orthopedics
-- ================================================================
INSERT INTO doctor_schedule (doctor_id, day_of_week, start_time, end_time, max_appointments, is_active)
VALUES

-- DR-001: General Medicine
((SELECT id FROM staff WHERE employee_id = 'DR-001'), 'MON', '09:00', '13:00', 20, TRUE),
((SELECT id FROM staff WHERE employee_id = 'DR-001'), 'WED', '09:00', '13:00', 20, TRUE),
((SELECT id FROM staff WHERE employee_id = 'DR-001'), 'FRI', '09:00', '17:00', 30, TRUE),

-- DR-002: Cardiology
((SELECT id FROM staff WHERE employee_id = 'DR-002'), 'MON', '10:00', '14:00', 15, TRUE),
((SELECT id FROM staff WHERE employee_id = 'DR-002'), 'TUE', '10:00', '14:00', 15, TRUE),
((SELECT id FROM staff WHERE employee_id = 'DR-002'), 'THU', '10:00', '16:00', 20, TRUE),

-- DR-003: Orthopedics
((SELECT id FROM staff WHERE employee_id = 'DR-003'), 'TUE', '08:00', '12:00', 15, TRUE),
((SELECT id FROM staff WHERE employee_id = 'DR-003'), 'WED', '08:00', '12:00', 15, TRUE),
((SELECT id FROM staff WHERE employee_id = 'DR-003'), 'SAT', '09:00', '13:00', 25, TRUE);


-- ================================================================
-- 5. LEAVE EXCEPTION
--    DR-001 takes Friday 2025-06-13 off.
--    DR-002 takes Thursday 2025-06-12 off.
--
--    How doctor_schedule_id is derived:
--      date 2025-06-13 → FRIDAY
--      SELECT id FROM doctor_schedule
--        WHERE doctor_id = DR-001 AND day_of_week = 'FRI'
--      → that id is used below.
-- ================================================================
INSERT INTO leave_exception (doctor_schedule_id, exception_date, reason)
VALUES
(
    -- DR-001's FRI slot
    (SELECT ds.id FROM doctor_schedule ds
     JOIN staff s ON s.id = ds.doctor_id
     WHERE s.employee_id = 'DR-001' AND ds.day_of_week = 'FRI'),
    '2025-06-13',
    'Personal leave'
),
(
    -- DR-002's THU slot
    (SELECT ds.id FROM doctor_schedule ds
     JOIN staff s ON s.id = ds.doctor_id
     WHERE s.employee_id = 'DR-002' AND ds.day_of_week = 'THU'),
    '2025-06-12',
    'Conference attendance'
);


-- ================================================================
-- 6. APPOINTMENTS
--    Sample appointments across two dates.
--    All booked by NR-001 (Sunita Patil).
--
--    Date 1: 2025-06-09 (Monday)
--      DR-001 → 3 appointments (WAITING, WAITING, COMPLETED)
--      DR-002 → 2 appointments (WAITING, CANCELLED)
--
--    Date 2: 2025-06-11 (Wednesday)
--      DR-001 → 2 appointments (WAITING, IN_PROGRESS)
--      DR-003 → 2 appointments (WAITING, WAITING)
-- ================================================================
INSERT INTO appointment (
    patient_id, doctor_id, booked_by,
    appointment_date, queue_number, status, notes
)
VALUES

-- 2025-06-09 · DR-001
(
    (SELECT id FROM patient WHERE mobile = '9100000001'),
    (SELECT id FROM staff  WHERE employee_id = 'DR-001'),
    (SELECT id FROM staff  WHERE employee_id = 'NR-001'),
    '2025-06-09', 1, 'COMPLETED', 'Fever and body ache'
),
(
    (SELECT id FROM patient WHERE mobile = '9100000002'),
    (SELECT id FROM staff  WHERE employee_id = 'DR-001'),
    (SELECT id FROM staff  WHERE employee_id = 'NR-001'),
    '2025-06-09', 2, 'WAITING', 'Routine check-up'
),
(
    (SELECT id FROM patient WHERE mobile = '9100000003'),
    (SELECT id FROM staff  WHERE employee_id = 'DR-001'),
    (SELECT id FROM staff  WHERE employee_id = 'NR-001'),
    '2025-06-09', 3, 'WAITING', 'Headache and dizziness'
),

-- 2025-06-09 · DR-002
(
    (SELECT id FROM patient WHERE mobile = '9100000004'),
    (SELECT id FROM staff  WHERE employee_id = 'DR-002'),
    (SELECT id FROM staff  WHERE employee_id = 'NR-001'),
    '2025-06-09', 1, 'WAITING', 'Chest pain follow-up'
),
(
    (SELECT id FROM patient WHERE mobile = '9100000005'),
    (SELECT id FROM staff  WHERE employee_id = 'DR-002'),
    (SELECT id FROM staff  WHERE employee_id = 'NR-001'),
    '2025-06-09', 2, 'CANCELLED', 'BP monitoring'
),

-- 2025-06-11 · DR-001
(
    (SELECT id FROM patient WHERE mobile = '9100000006'),
    (SELECT id FROM staff  WHERE employee_id = 'DR-001'),
    (SELECT id FROM staff  WHERE employee_id = 'NR-001'),
    '2025-06-11', 1, 'IN_PROGRESS', 'Diabetes review'
),
(
    (SELECT id FROM patient WHERE mobile = '9100000007'),
    (SELECT id FROM staff  WHERE employee_id = 'DR-001'),
    (SELECT id FROM staff  WHERE employee_id = 'NR-001'),
    '2025-06-11', 2, 'WAITING', 'Cold and cough'
),

-- 2025-06-11 · DR-003
(
    (SELECT id FROM patient WHERE mobile = '9100000008'),
    (SELECT id FROM staff  WHERE employee_id = 'DR-003'),
    (SELECT id FROM staff  WHERE employee_id = 'NR-001'),
    '2025-06-11', 1, 'WAITING', 'Knee pain'
),
(
    (SELECT id FROM patient WHERE mobile = '9100000009'),
    (SELECT id FROM staff  WHERE employee_id = 'DR-003'),
    (SELECT id FROM staff  WHERE employee_id = 'NR-001'),
    '2025-06-11', 2, 'WAITING', 'Post-fracture follow-up'
);


-- ================================================================
-- Update cancelled appointment — set cancellation metadata.
-- (The appointment itself was inserted above with status CANCELLED)
-- ================================================================
UPDATE appointment
SET
    cancelled_by = (SELECT id FROM staff WHERE employee_id = 'NR-001'),
    cancelled_at = NOW()
WHERE
    doctor_id        = (SELECT id FROM staff WHERE employee_id = 'DR-002')
    AND appointment_date = '2025-06-09'
    AND queue_number     = 2;


-- ================================================================
-- 7. CONSULTATION
--    Only for appointments that are IN_PROGRESS or COMPLETED.
--
--    COMPLETED appointment (DR-001, 2025-06-09, queue 1) →
--      consultation is locked with completed_at set.
--
--    IN_PROGRESS appointment (DR-001, 2025-06-11, queue 1) →
--      consultation is open (is_locked = FALSE, no completed_at).
-- ================================================================
INSERT INTO consultation (
    appointment_id, doctor_id,
    clinical_notes, diagnosis,
    is_locked, started_at, completed_at
)
VALUES

-- COMPLETED consultation (locked)
(
    (SELECT a.id FROM appointment a
     JOIN staff s ON s.id = a.doctor_id
     WHERE s.employee_id = 'DR-001'
       AND a.appointment_date = '2025-06-09'
       AND a.queue_number = 1),
    (SELECT id FROM staff WHERE employee_id = 'DR-001'),
    'Patient presented with high-grade fever (102°F) and generalised body ache for 3 days. No cough or cold. Throat mildly inflamed.',
    'Viral fever — likely influenza. No bacterial secondary infection.',
    TRUE,
    '2025-06-09 09:15:00+05:30',
    '2025-06-09 09:35:00+05:30'
),

-- IN_PROGRESS consultation (not locked)
(
    (SELECT a.id FROM appointment a
     JOIN staff s ON s.id = a.doctor_id
     WHERE s.employee_id = 'DR-001'
       AND a.appointment_date = '2025-06-11'
       AND a.queue_number = 1),
    (SELECT id FROM staff WHERE employee_id = 'DR-001'),
    'Patient with known Type 2 DM. Reports fasting sugar ~180 mg/dL. Mild fatigue. No hypoglycaemic episodes.',
    NULL,
    FALSE,
    '2025-06-11 09:10:00+05:30',
    NULL
);


-- ================================================================
-- 8. PRESCRIPTION  +  PRESCRIPTION_MEDICINE
--    Only for the COMPLETED consultation (locked).
-- ================================================================
INSERT INTO prescription (
    consultation_id,
    general_instructions,
    follow_up_notes,
    follow_up_date,
    is_locked
)
VALUES (
    (SELECT c.id FROM consultation c
     JOIN appointment a ON a.id = c.appointment_id
     JOIN staff s       ON s.id = a.doctor_id
     WHERE s.employee_id      = 'DR-001'
       AND a.appointment_date = '2025-06-09'
       AND a.queue_number     = 1),
    'Take medicines on time. Drink at least 3 litres of water daily. Rest for 2 days. Avoid cold food.',
    'Return if fever persists beyond 5 days or new symptoms develop.',
    '2025-06-14',
    TRUE
);

-- Medicine line items for the prescription above
INSERT INTO prescription_medicine (
    prescription_id,
    medicine_name, medicine_category, medicine_unit,
    dosage, frequency, duration_days, instructions, sort_order
)
VALUES

-- 1. Paracetamol
(
    (SELECT p.id FROM prescription p
     JOIN consultation c ON c.id = p.consultation_id
     JOIN appointment  a ON a.id = c.appointment_id
     JOIN staff        s ON s.id = a.doctor_id
     WHERE s.employee_id      = 'DR-001'
       AND a.appointment_date = '2025-06-09'
       AND a.queue_number     = 1),
    'Paracetamol', 'Analgesic / Antipyretic', 'mg',
    '500mg', 'Three times daily', 5, 'After food', 1
),

-- 2. Cetirizine
(
    (SELECT p.id FROM prescription p
     JOIN consultation c ON c.id = p.consultation_id
     JOIN appointment  a ON a.id = c.appointment_id
     JOIN staff        s ON s.id = a.doctor_id
     WHERE s.employee_id      = 'DR-001'
       AND a.appointment_date = '2025-06-09'
       AND a.queue_number     = 1),
    'Cetirizine', 'Antihistamine', 'mg',
    '10mg', 'Once daily at night', 5, 'Before sleep', 2
),

-- 3. ORS Sachet
(
    (SELECT p.id FROM prescription p
     JOIN consultation c ON c.id = p.consultation_id
     JOIN appointment  a ON a.id = c.appointment_id
     JOIN staff        s ON s.id = a.doctor_id
     WHERE s.employee_id      = 'DR-001'
       AND a.appointment_date = '2025-06-09'
       AND a.queue_number     = 1),
    'ORS Sachet', 'Oral Rehydration', 'sachet',
    '1 sachet in 200ml water', 'Twice daily', 3, 'Mix in clean water and drink slowly', 3
);
