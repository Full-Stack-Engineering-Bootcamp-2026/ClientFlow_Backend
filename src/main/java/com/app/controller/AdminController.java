package com.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.dto.CreateStaffRequest;
import com.app.dto.CreateStaffResponse;
import com.app.dto.DashboardSummaryResponse;
import com.app.dto.StaffResponse;
import com.app.dto.UpdateStaffStatusRequest;

import com.app.response.ApiResponse;
import com.app.service.AdminService;
import com.app.service.StaffService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class AdminController {

    private final StaffService staffService;

    private final AdminService adminService;

    @PostMapping("/staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CreateStaffResponse>> createStaff(
            @Valid @RequestBody CreateStaffRequest request) {
        CreateStaffResponse response = staffService.createStaff(request);

        return ResponseEntity.ok(
                ApiResponse.<CreateStaffResponse>builder()
                        .success(true)
                        .message("Staff created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<StaffResponse>>> getAllStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<StaffResponse> data = staffService.getAllStaff(page, size);

        return ResponseEntity.ok(
                ApiResponse.<Page<StaffResponse>>builder()
                        .success(true)
                        .message("Staff fetched successfully")
                        .data(data)
                        .build());
    }

    @PatchMapping("/staff/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateStaffStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStaffStatusRequest request) {

        staffService.updateStaffStatus(id, request.getIsActive());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Staff status updated successfully")
                        .build());
    }

    @GetMapping("/dashboard/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getDashboardSummary() {

        DashboardSummaryResponse response = adminService.getDashboardSummary();

        return ResponseEntity.ok(
                ApiResponse.<DashboardSummaryResponse>builder()
                        .success(true)
                        .message("Dashboard summary fetched successfully")
                        .data(response)
                        .build()
        );
     }

     
    
}