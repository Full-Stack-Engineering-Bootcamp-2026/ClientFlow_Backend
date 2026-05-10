package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryResponse {

    private long totalStaff;
    private long activeDoctors;
    private long totalAppointments;
    private long completedAppointments;
    private long cancelledAppointments;
}