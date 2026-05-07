package com.app.controller;

import com.app.dto.patient.request.BookAppointmentRequest;
import com.app.dto.patient.response.AppointmentBookingResponse;
import com.app.dto.patient.response.DoctorDropdownResponse;
import com.app.dto.patient.response.PatientSearchResponse;
import com.app.response.ApiResponse;
import com.app.service.patient.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/nurse/appointments")
@RequiredArgsConstructor
public class NurseAppointmentController {

    private final AppointmentService appointmentService;
    @GetMapping("/doctors")
@PreAuthorize("hasAnyRole('ADMIN','NURSE')")
public ResponseEntity<ApiResponse<List<DoctorDropdownResponse>>> getDoctors() {

    return ResponseEntity.ok(
            ApiResponse.<List<DoctorDropdownResponse>>builder()
                    .success(true)
                    .message("Doctors fetched successfully")
                    .data(appointmentService.getAllDoctors())
                    .build()
    );
}

    @GetMapping("/patients/search")
    @PreAuthorize("hasAnyRole('ADMIN','NURSE')")
    public ResponseEntity<ApiResponse<List<PatientSearchResponse>>> searchPatients(
            @RequestParam String keyword
    ) {

        List<PatientSearchResponse> response = appointmentService
                .searchPatients(keyword);

        return ResponseEntity.ok(
                ApiResponse.<List<PatientSearchResponse>>builder()
                        .success(true)
                        .message("Patients fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','NURSE')")
    public ResponseEntity<ApiResponse<AppointmentBookingResponse>> bookAppointment(
            @Valid @RequestBody BookAppointmentRequest request
    ) {

        AppointmentBookingResponse response = appointmentService
                .bookAppointment(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<AppointmentBookingResponse>builder()
                                .success(true)
                                .message("Appointment booked successfully")
                                .data(response)
                                .build()
                );
    }
}