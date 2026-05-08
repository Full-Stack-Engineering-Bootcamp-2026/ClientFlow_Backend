package com.app.repository;
import java.util.List;
import com.app.entity.DoctorSchedule;
import com.app.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeekAndIsActiveTrue(
            Long doctorId,
            DayOfWeek dayOfWeek
    );
    List<DoctorSchedule> findByDoctorIdAndIsActiveTrue(Long doctorId);

boolean existsByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek day);

long countByDayOfWeekAndIsActiveTrue(com.app.enums.DayOfWeek dayOfWeek);
}