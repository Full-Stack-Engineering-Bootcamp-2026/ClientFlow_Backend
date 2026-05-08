package com.app.controller;

import com.app.dto.DoctorDashboardResponse;
import com.app.response.ApiResponse;
import com.app.service.DoctorQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor/queue")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorQueueController {

    private final DoctorQueueService doctorQueueService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DoctorDashboardResponse>> getDashboard() {

        return ResponseEntity.ok(
                ApiResponse.<DoctorDashboardResponse>builder()
                        .success(true)
                        .message("Dashboard fetched successfully")
                        .data(doctorQueueService.getDashboard())
                        .build()
        );
    }
}