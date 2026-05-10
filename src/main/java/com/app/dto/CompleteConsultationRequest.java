package com.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CompleteConsultationRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    @NotBlank(message = "Clinical notes are required")
    private String clinicalNotes;

    private String generalInstructions;

    private LocalDate followUpDate;

    private String followUpNotes;

    @Valid
    private List<MedicineRequest> medicines;
}