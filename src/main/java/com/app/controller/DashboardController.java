package com.app.controller;

import com.app.dto.DoctorScheduleDashboardResponse;
import com.app.response.ApiResponse;
import com.app.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/doctor-schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<DoctorScheduleDashboardResponse>>>
    getDoctorScheduleDashboard(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<DoctorScheduleDashboardResponse>>builder()
                        .success(true)
                        .message("Doctor schedule dashboard fetched successfully")
                        .data(
                                dashboardService
                                        .getDoctorScheduleDashboard(startDate)
                        )
                        .build()
        );
    }
}