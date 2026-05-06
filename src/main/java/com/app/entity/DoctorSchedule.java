package com.app.entity;

import com.app.entity.base.CreatedOnly;
import com.app.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = false)
@Entity
@Table(
    name = "doctor_schedule",
    uniqueConstraints = @UniqueConstraint(name = "uq_doctor_day", columnNames = {"doctor_id", "day_of_week"})
)
public class DoctorSchedule extends CreatedOnly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Staff doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 3)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Builder.Default
    @Column(name = "max_appointments", nullable = false)
    private Integer maxAppointments = 20;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}