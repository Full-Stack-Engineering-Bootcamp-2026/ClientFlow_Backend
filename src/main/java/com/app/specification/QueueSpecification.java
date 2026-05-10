package com.app.specification;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class QueueSpecification {

    private QueueSpecification() {
    }

    public static Specification<Appointment> queueForDate(
            LocalDate appointmentDate
    ) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("appointmentDate"),
                        appointmentDate
                );
    }

    public static Specification<Appointment> appointmentStatuses(
            List<AppointmentStatus> statuses
    ) {

        return (root, query, cb) ->
                root.get("status").in(statuses);
    }

    public static Specification<Appointment> doctorAppointments(
            Long doctorId
    ) {

        return (root, query, cb) ->
                cb.equal(root.get("doctor").get("id"), doctorId);
    }
}