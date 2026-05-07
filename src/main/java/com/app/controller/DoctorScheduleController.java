package com.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.DoctorScheduleRequest;
import com.app.dto.DoctorScheduleResponse;
import com.app.dto.DoctorWeeklyScheduleRequest;
import com.app.response.ApiResponse;
import com.app.service.DoctorScheduleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/schedules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> create(
            @Valid @RequestBody DoctorScheduleRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<DoctorScheduleResponse>builder()
                        .success(true)
                        .message("Schedule created successfully")
                        .data(scheduleService.createSchedule(request))
                        .build());
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponse>>> get(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                ApiResponse.<List<DoctorScheduleResponse>>builder()
                        .success(true)
                        .data(scheduleService.getSchedule(doctorId))
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody DoctorScheduleRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<DoctorScheduleResponse>builder()
                        .success(true)
                        .data(scheduleService.updateSchedule(id, request))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        scheduleService.deleteSchedule(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Schedule deleted successfully")
                        .build());
    }

    @PostMapping("/weekly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> createWeeklySchedule(
            @RequestBody DoctorWeeklyScheduleRequest request) {

        scheduleService.createWeeklySchedule(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Weekly schedule created")
                        .build());
    }
}