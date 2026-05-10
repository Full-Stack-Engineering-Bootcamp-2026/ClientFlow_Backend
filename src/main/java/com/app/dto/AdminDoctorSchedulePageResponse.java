package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminDoctorSchedulePageResponse {

    private List<AdminDoctorScheduleRowResponse> content;
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;
    private AdminDoctorScheduleStatsResponse stats;
    private List<String> specializations;
}