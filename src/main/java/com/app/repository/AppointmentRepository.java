package com.app.repository;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    boolean existsByPatientId(Long patientId);

    long countByDoctorIdAndAppointmentDateAndStatusIn(
            Long doctorId,
            LocalDate appointmentDate,
            List<AppointmentStatus> statuses);

    // @Query("""
    // SELECT COALESCE(MAX(a.queueNumber),0)
    // FROM Appointment a
    // WHERE a.doctor.id = :doctorId
    // AND a.appointmentDate = :appointmentDate
    // """)
    // Integer findMaxQueueNumber(
    // @Param("doctorId") Long doctorId,
    // @Param("appointmentDate") LocalDate appointmentDate
    // );
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
}