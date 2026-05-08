package com.app.dao;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;
import com.app.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class AppointmentDaoImpl implements AppointmentDao{

    private final AppointmentRepository appointmentRepository;

    public long getTotalAppointmentsCount() {
        return appointmentRepository.count();
    }

    public long getCompletedAppointmentsCount() {
        return appointmentRepository.countByStatus(AppointmentStatus.COMPLETED);
    }

    public long getCancelledAppointmentsCount() {
        return appointmentRepository.countByStatus(AppointmentStatus.CANCELLED);
    }

    public boolean existsPatientBooking(Long doctorId,Long patientId,LocalDate date) {

        return appointmentRepository
                .existsByDoctorIdAndPatientIdAndAppointmentDate(doctorId,patientId,date
                );
    }

    public long countDoctorAppointments(Long doctorId,LocalDate date) {

        return appointmentRepository
                .countByDoctorIdAndAppointmentDate(doctorId,date);
    }

    public int getNextQueueNumber(Long doctorId,LocalDate date) {

        return appointmentRepository
                .findTopByDoctorIdAndAppointmentDateOrderByQueueNumberDesc(doctorId,date)
                .map(a -> a.getQueueNumber() + 1)
                .orElse(1);
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    public long countAppointmentsByDate(LocalDate date) {

    return appointmentRepository
            .countByAppointmentDate(date);
}
}