package com.app.repository;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

        boolean existsByPatientId(Long patientId);

        long countByDoctorIdAndAppointmentDateAndStatusIn(
                        Long doctorId,
                        LocalDate appointmentDate,
                        List<AppointmentStatus> statuses);

        long countByStatus(AppointmentStatus status);

        long countByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

        Optional<Appointment> findTopByDoctorIdAndAppointmentDateOrderByQueueNumberDesc(Long doctorId, LocalDate date);

        boolean existsByDoctorIdAndPatientIdAndAppointmentDate(Long doctorId, Long patientId, LocalDate date);

        long countByAppointmentDate(LocalDate appointmentDate);

        Optional<Appointment> findFirstByDoctorIdAndAppointmentDateAndStatus(
                        Long doctorId,
                        LocalDate appointmentDate,
                        AppointmentStatus status);

        List<Appointment> findByDoctorIdAndAppointmentDateAndStatusOrderByQueueNumberAsc(
                        Long doctorId,
                        LocalDate appointmentDate,
                        AppointmentStatus status);

        // @Lock(LockModeType.PESSIMISTIC_WRITE)
        // Optional<Appointment> findTopByDoctorIdAndAppointmentDateOrderByQueueNumberDescForUpdate(
        //                 Long doctorId,
        //                 LocalDate date);
}