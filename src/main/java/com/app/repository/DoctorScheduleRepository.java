package com.app.repository;

import com.app.entity.DoctorSchedule;
import com.app.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeekAndIsActiveTrue(
            Long doctorId,
            DayOfWeek dayOfWeek
    );
}