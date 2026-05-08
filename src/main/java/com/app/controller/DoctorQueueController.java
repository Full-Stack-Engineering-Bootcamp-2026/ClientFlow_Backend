package com.app.controller;

import com.app.dto.CallNextPatientRequest;
import com.app.dto.CallNextPatientResponse;
import com.app.dto.CompleteConsultationRequest;
import com.app.dto.ConsultationPageResponse;
import com.app.dto.DoctorDashboardResponse;
import com.app.response.ApiResponse;
import com.app.service.DoctorQueueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
                                                .build());
        }

        @PostMapping("/call-next")
        public ResponseEntity<ApiResponse<CallNextPatientResponse>> callNextPatient(
                        @Valid @RequestBody CallNextPatientRequest request) {

                return ResponseEntity.ok(
                                ApiResponse.<CallNextPatientResponse>builder()
                                                .success(true)
                                                .message("Patient moved to current queue")
                                                .data(doctorQueueService.callNextPatient(request))
                                                .build());
        }

        @GetMapping("/consultations/appointment/{appointmentId}")
        public ResponseEntity<ApiResponse<ConsultationPageResponse>> getConsultationPage(
                        @PathVariable Long appointmentId) {

                return ResponseEntity.ok(
                                ApiResponse.<ConsultationPageResponse>builder()
                                                .success(true)
                                                .message("Consultation page fetched successfully")
                                                .data(
                                                                doctorQueueService
                                                                                .getConsultationPage(appointmentId))
                                                .build());
        }

        @PostMapping("/consultations/complete")
        public ResponseEntity<ApiResponse<Void>> completeConsultation(
                        @Valid @RequestBody CompleteConsultationRequest request) {

                doctorQueueService.completeConsultation(request);

                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .success(true)
                                                .message("Consultation completed successfully")
                                                .build());
        }
}