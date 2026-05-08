package com.app.controller;

import com.app.dto.patient.request.RegisterPatientRequest;
import com.app.dto.patient.response.PatientResponse;
import com.app.response.ApiResponse;
import com.app.service.patient.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nurse/patients")
@RequiredArgsConstructor
public class NursePatientController {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','NURSE')")
    public ResponseEntity<ApiResponse<PatientResponse>> registerPatient(
            @Valid @RequestBody RegisterPatientRequest request
    ) {

        PatientResponse response = patientService.registerPatient(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<PatientResponse>builder()
                                .success(true)
                                .message("Patient registered successfully")
                                .data(response)
                                .build()
                );
    }
}