package com.app.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    long countByStatus(AppointmentStatus status);

    long countByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

    Optional<Appointment> findTopByDoctorIdAndAppointmentDateOrderByQueueNumberDesc(Long doctorId,LocalDate date);

    boolean existsByDoctorIdAndPatientIdAndAppointmentDate(Long doctorId,Long patientId,LocalDate date);

long countByAppointmentDate(LocalDate appointmentDate);
    
}