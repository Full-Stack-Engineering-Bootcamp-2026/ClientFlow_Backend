package com.app.controller;

import com.app.dto.patient.response.LiveQueueDashboardResponse;
import com.app.service.patient.LiveQueueService;
import com.app.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nurse/live-queue")
@RequiredArgsConstructor
public class LiveQueueController {

    private final LiveQueueService liveQueueService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','NURSE')")
    public ResponseEntity<ApiResponse<LiveQueueDashboardResponse>> getLiveQueue() {

        return ResponseEntity.ok(
                ApiResponse.<LiveQueueDashboardResponse>builder()
                        .success(true)
                        .message("Live queue fetched successfully")
                        .data(liveQueueService.getLiveQueue())
                        .build()
        );
    }
}