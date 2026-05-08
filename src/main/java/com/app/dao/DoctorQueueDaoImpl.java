package com.app.dao;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;
import com.app.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DoctorQueueDaoImpl implements DoctorQueueDao {

    private final AppointmentRepository appointmentRepository;

    @Override
    public long countByDoctorAndStatus(
            Long doctorId,
            LocalDate date,
            List<AppointmentStatus> statuses) {

        return appointmentRepository.countByDoctorIdAndAppointmentDateAndStatusIn(
                doctorId,
                date,
                statuses);
    }

    @Override
    public long countByDoctorAndDate(
            Long doctorId,
            LocalDate date) {

        return appointmentRepository.countByDoctorIdAndAppointmentDate(
                doctorId,
                date);
    }

    @Override
    public Optional<Appointment> getCurrentPatient(
            Long doctorId,
            LocalDate date) {

        return appointmentRepository
                .findFirstByDoctorIdAndAppointmentDateAndStatus(
                        doctorId,
                        date,
                        AppointmentStatus.IN_PROGRESS);
    }

    @Override
    public List<Appointment> getWaitingPatients(
            Long doctorId,
            LocalDate date) {

        return appointmentRepository
                .findByDoctorIdAndAppointmentDateAndStatusOrderByQueueNumberAsc(
                        doctorId,
                        date,
                        AppointmentStatus.WAITING);
    }
}