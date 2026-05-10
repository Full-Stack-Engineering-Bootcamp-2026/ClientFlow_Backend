package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminDoctorScheduleRowResponse {

    private Long doctorId;
    private String doctorName;
    private String specialization;
    private String startTime;
    private String endTime;
    private Integer maxAppointments;
    private List<String> workingDays;
    private List<String> leaveDates;
    private Boolean onLeaveThisWeek;
}