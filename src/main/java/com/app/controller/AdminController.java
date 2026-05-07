package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.CreateStaffRequest;
import com.app.dto.CreateStaffResponse;
import com.app.repository.StaffRepository;
import com.app.response.ApiResponse;
import com.app.service.StaffService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class AdminController {

    private final StaffService staffService;

    @PostMapping("/staff")
    @PreAuthorize("hasRole('ADMIN')")
     public ResponseEntity<ApiResponse<CreateStaffResponse>> createStaff(@Valid @RequestBody CreateStaffRequest request){
        CreateStaffResponse response = staffService.createStaff(request);

        return ResponseEntity.ok(
                ApiResponse.<CreateStaffResponse>builder()
                        .success(true)
                        .message("Staff created successfully")
                        .data(response)
                        .build()
        );
     }

     
    
}