package com.app.dao.patient.impl;

import com.app.dao.patient.AppointmentDao;
import com.app.entity.Appointment;
import com.app.exception.BadRequestException;
import com.app.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AppointmentDaoImpl implements AppointmentDao {

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public Appointment save(Appointment appointment) {

        try {
            return appointmentRepository.save(appointment);

        } catch (DataAccessException ex) {

            log.error("Database error while booking appointment: {}", ex.getMessage());

            throw new BadRequestException(
                    "Unable to book appointment"
            );
        }
    }
}