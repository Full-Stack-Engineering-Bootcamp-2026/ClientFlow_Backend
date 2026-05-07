package com.app.specification;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class AppointmentSpecification {

    private AppointmentSpecification() {
    }

    public static Specification<Appointment> doctorAppointmentsForDate(
            Long doctorId,
            LocalDate appointmentDate,
            List<AppointmentStatus> statuses
    ) {

        return (root, query, cb) ->
                cb.and(
                        cb.equal(root.get("doctor").get("id"), doctorId),
                        cb.equal(root.get("appointmentDate"), appointmentDate),
                        root.get("status").in(statuses)
                );
    }
}