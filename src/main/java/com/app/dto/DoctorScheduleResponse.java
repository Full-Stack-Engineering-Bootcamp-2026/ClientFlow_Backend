package com.app.dto;

import com.app.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class DoctorScheduleResponse {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxAppointments;
    private Boolean isActive;
}