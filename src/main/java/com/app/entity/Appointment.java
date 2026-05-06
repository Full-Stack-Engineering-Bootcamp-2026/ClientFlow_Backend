package com.app.entity;

import com.app.entity.base.Auditable;
import com.app.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = false)
@Entity
@Table(
    name = "appointment",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_queue_slot",
        columnNames = {"doctor_id", "appointment_date", "queue_number"}
    ),
    indexes = {
        @Index(name = "idx_appt_queue",   columnList = "doctor_id, appointment_date, status"),
        @Index(name = "idx_appt_patient", columnList = "patient_id")
    }
)
public class Appointment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Staff doctor;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booked_by", nullable = false)
    private Staff bookedBy;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "queue_number", nullable = false)
    private Integer queueNumber;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AppointmentStatus status = AppointmentStatus.WAITING;

    @Column(name = "notes", length = 500)
    private String notes;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private Staff cancelledBy;
}