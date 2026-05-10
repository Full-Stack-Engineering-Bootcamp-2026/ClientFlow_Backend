package com.app.controller;

import com.app.dto.DoctorScheduleDashboardResponse;
import com.app.dto.StaffResponse;
import com.app.response.ApiResponse;
import com.app.service.DashboardService;
import com.app.service.StaffService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
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

    private final StaffService staffService;

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

    @GetMapping("/recent-staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<StaffResponse>>> getRecentStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Page<StaffResponse> data = staffService.getAllStaff(
                page,
                size,
                null,
                null,
                null
        );

        return ResponseEntity.ok(
                ApiResponse.<Page<StaffResponse>>builder()
                        .success(true)
                        .message("Recent staff fetched successfully")
                        .data(data)
                        .build()
        );
    }
}