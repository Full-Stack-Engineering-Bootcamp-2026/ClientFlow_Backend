package com.app.dao;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorQueueDao {

    long countByDoctorAndStatus(
            Long doctorId,
            LocalDate date,
            List<AppointmentStatus> statuses
    );

    long countByDoctorAndDate(
            Long doctorId,
            LocalDate date
    );

    Optional<Appointment> getCurrentPatient(
            Long doctorId,
            LocalDate date
    );

    List<Appointment> getWaitingPatients(
            Long doctorId,
            LocalDate date
    );
}