package com.app.validator;

import com.app.entity.DoctorSchedule;
import com.app.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class AppointmentValidator {

    public void validateDoctorCapacity(
            long existingAppointments,
            DoctorSchedule doctorSchedule
    ) {

        if (existingAppointments >= doctorSchedule.getMaxAppointments()) {

            throw new BadRequestException(
                    "Doctor appointment limit reached for selected date"
            );
        }
    }

    public void validateDoctorActive(Boolean isActive) {

        if (!Boolean.TRUE.equals(isActive)) {

            throw new BadRequestException(
                    "Doctor is inactive"
            );
        }
    }
}