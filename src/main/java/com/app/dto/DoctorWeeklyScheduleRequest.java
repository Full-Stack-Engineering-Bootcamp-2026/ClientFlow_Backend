package com.app.dto;

import java.time.LocalTime;
import java.util.List;

import com.app.enums.DayOfWeek;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DoctorWeeklyScheduleRequest {

    @NotNull
    private Long doctorId;

    @NotEmpty
    private List<DayOfWeek> days;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private Integer maxAppointments;
}