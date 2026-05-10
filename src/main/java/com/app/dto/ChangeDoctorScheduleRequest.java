package com.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.Min;
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

    @Min(value = 1, message = "Max appointments must be at least 1")
    private Integer maxAppointments;

    private String reason;
}