package com.app.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateDoctorScheduleRequest {

    private LocalTime startTime;

    private LocalTime endTime;

    @Min(value = 1, message = "Max appointments must be at least 1")
    private Integer maxAppointments;

    private Boolean isActive;

}