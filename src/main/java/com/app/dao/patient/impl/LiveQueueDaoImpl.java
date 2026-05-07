package com.app.dao.patient.impl;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;
import com.app.exception.BadRequestException;
import com.app.dao.patient.LiveQueueDao;
import com.app.specification.QueueSpecification;
import com.app.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LiveQueueDaoImpl implements LiveQueueDao {

    private final AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getTodayAppointments(LocalDate appointmentDate) {

        try {

            Specification<Appointment> specification =
                    QueueSpecification.queueForDate(appointmentDate)
                            .and(
                                    QueueSpecification.appointmentStatuses(
                                            List.of(
                                                    AppointmentStatus.WAITING,
                                                    AppointmentStatus.IN_PROGRESS,
                                                    AppointmentStatus.COMPLETED
                                            )
                                    )
                            );

            return appointmentRepository.findAll(specification);

        } catch (DataAccessException ex) {

            log.error("Database error while fetching live queue: {}", ex.getMessage());

            throw new BadRequestException(
                    "Unable to fetch live queue"
            );
        }
    }
}