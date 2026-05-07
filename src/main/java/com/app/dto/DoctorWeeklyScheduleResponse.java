package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DoctorWeeklyScheduleResponse {

    private Long doctorId;
    private String doctorName;
    private List<String> workingDays;
    private List<String> leaveDates;
}