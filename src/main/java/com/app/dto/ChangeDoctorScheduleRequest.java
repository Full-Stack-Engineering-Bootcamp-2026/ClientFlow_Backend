package com.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ChangeDoctorScheduleRequest {

    @NotNull
    private Long doctorId;

    @NotEmpty
    private List<LocalDate> dates;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer maxAppointments;

    private String reason;
}