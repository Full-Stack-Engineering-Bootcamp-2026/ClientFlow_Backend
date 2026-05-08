package com.app.dto;

import java.time.LocalTime;
import lombok.Data;

@Data

public class UpdateDoctorScheduleRequest {

        private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxAppointments;
    private Boolean isActive;

}