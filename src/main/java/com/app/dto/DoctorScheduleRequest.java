package com.app.dto;

import com.app.enums.DayOfWeek;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalTime;


@Data
public class DoctorScheduleRequest {

    @NotNull
    private Long doctorId;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @Min(1)
    private Integer maxAppointments;

    private Boolean isActive;
}