package com.app.repository;

import com.app.entity.LeaveException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface LeaveExceptionRepository extends JpaRepository<LeaveException, Long> {

    boolean existsByDoctorScheduleDoctorIdAndExceptionDate(
            Long doctorId,
            LocalDate date
    );
}