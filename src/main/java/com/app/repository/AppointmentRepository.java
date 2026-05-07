package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    long countByStatus(AppointmentStatus status);
    
}