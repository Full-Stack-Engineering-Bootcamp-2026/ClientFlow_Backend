package com.app.dao;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;
import com.app.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.springframework.dao.DataAccessException;

import com.app.exception.BadRequestException;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class AppointmentDaoImpl implements AppointmentDao{

    private final AppointmentRepository appointmentRepository;
    @PersistenceContext
private EntityManager entityManager;

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

@Override
public Integer findMaxQueueNumber(
        Long doctorId,
        LocalDate appointmentDate
) {

    try {

        CriteriaBuilder cb =
                entityManager.getCriteriaBuilder();

        CriteriaQuery<Integer> query =
                cb.createQuery(Integer.class);

        Root<Appointment> root =
                query.from(Appointment.class);

        query.select(
                cb.coalesce(
                        cb.max(root.get("queueNumber")),
                        0
                )
        );

        query.where(
                cb.and(
                        cb.equal(
                                root.get("doctor").get("id"),
                                doctorId
                        ),
                        cb.equal(
                                root.get("appointmentDate"),
                                appointmentDate
                        )
                )
        );

        TypedQuery<Integer> typedQuery =
                entityManager.createQuery(query);

        return typedQuery.getSingleResult();

    } catch (DataAccessException ex) {

        throw new BadRequestException(
                "Unable to generate queue number"
        );
    }
}
}