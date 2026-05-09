package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.PatientHistoryResponse;
import com.app.response.ApiResponse;
import com.app.service.PatientHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('DOCTOR','NURSE','ADMIN')")
public class PatientHistoryController {

    private final PatientHistoryService patientHistoryService;

    @GetMapping("/{patientId}/history")
    public ResponseEntity<ApiResponse<PatientHistoryResponse>>
    getPatientHistory(
            @PathVariable Long patientId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<PatientHistoryResponse>builder()
                        .success(true)
                        .message("Patient history fetched successfully")
                        .data(
                                patientHistoryService
                                        .getPatientHistory(patientId)
                        )
                        .build()
        );
    }
}