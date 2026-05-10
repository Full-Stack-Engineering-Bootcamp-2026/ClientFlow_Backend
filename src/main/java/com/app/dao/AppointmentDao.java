package com.app.dao;

import com.app.entity.Appointment;

import java.time.LocalDate;

public interface AppointmentDao {

    long getTotalAppointmentsCount();

    long getCompletedAppointmentsCount();

    long getCancelledAppointmentsCount();

    boolean existsPatientBooking(
            Long doctorId,
            Long patientId,
            LocalDate date
    );

    long countDoctorAppointments(
            Long doctorId,
            LocalDate date
    );

    int getNextQueueNumber(
            Long doctorId,
            LocalDate date
    );

    Appointment save(Appointment appointment);

    long countAppointmentsByDate(LocalDate date);

    Integer findMaxQueueNumber(
            Long doctorId,
            LocalDate appointmentDate
    );
}