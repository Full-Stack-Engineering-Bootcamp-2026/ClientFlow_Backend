package com.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.LeaveException;

public interface LeaveRepository extends JpaRepository<LeaveException, Long> {

    boolean existsByDoctorScheduleIdAndExceptionDate(Long scheduleId, LocalDate date);

    List<LeaveException> findByDoctorScheduleDoctorId(Long doctorId);

    List<LeaveException> findByDoctorScheduleDoctorIdAndExceptionDateBetween(
    Long doctorId,
    LocalDate start,
    LocalDate end
);
}