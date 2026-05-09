package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDoctorScheduleStatsResponse {

    private long activeClinicians;
    private long queueCapacityToday;
    private long onLeaveThisWeek;
}