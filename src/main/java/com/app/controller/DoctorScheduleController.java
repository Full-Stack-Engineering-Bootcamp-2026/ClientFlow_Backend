package com.app.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AdminDoctorSchedulePageResponse;
import com.app.dto.ChangeDoctorScheduleRequest;
import com.app.dto.DoctorScheduleRequest;
import com.app.dto.DoctorScheduleResponse;
import com.app.dto.DoctorWeeklyScheduleRequest;
import com.app.dto.DoctorWeeklyScheduleResponse;
import com.app.dto.UpdateDoctorScheduleRequest;
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

    // @PutMapping("/{id}")
    // public ResponseEntity<ApiResponse<DoctorScheduleResponse>> update(
    //         @PathVariable Long id,
    //         @Valid @RequestBody DoctorScheduleRequest request) {

    //     return ResponseEntity.ok(
    //             ApiResponse.<DoctorScheduleResponse>builder()
    //                     .success(true)
    //                     .data(scheduleService.updateSchedule(id, request))
    //                     .build());
    // }

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
           @Valid @RequestBody DoctorWeeklyScheduleRequest request) {

        scheduleService.createWeeklySchedule(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Weekly schedule created")
                        .build());
    }

    @GetMapping("/weekly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<DoctorWeeklyScheduleResponse>>> getWeekly(
            @RequestParam LocalDate startDate) {

        return ResponseEntity.ok(
                ApiResponse.<List<DoctorWeeklyScheduleResponse>>builder()
                        .success(true)
                        .message("Weekly schedule fetched successfully")
                        .data(scheduleService.getWeeklySchedule(startDate))
                        .build());
    }

    @GetMapping("/admin-view")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDoctorSchedulePageResponse>> getAdminScheduleView(
            @RequestParam LocalDate startDate,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(
                ApiResponse.<AdminDoctorSchedulePageResponse>builder()
                        .success(true)
                        .message("Doctor schedule view fetched successfully")
                        .data(scheduleService.getAdminSchedulePage(
                                startDate,
                                specialization,
                                status,
                                page,
                                size
                        ))
                        .build()
        );
    }

    @PostMapping("/change")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> changeSchedule(
            @Valid @RequestBody ChangeDoctorScheduleRequest request) {

        scheduleService.changeDoctorSchedule(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Schedule changed successfully")
                        .build()
        );
    }

    @PutMapping("/staff/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody UpdateDoctorScheduleRequest request) {

        scheduleService.updateSchedule(scheduleId, request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Schedule updated successfully")
                        .build());
    }
}