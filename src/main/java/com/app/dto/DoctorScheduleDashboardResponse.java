package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DoctorScheduleDashboardResponse {

    private String day;
    private LocalDate date;
    private Long doctorCount;
    private Long appointmentCount;
}