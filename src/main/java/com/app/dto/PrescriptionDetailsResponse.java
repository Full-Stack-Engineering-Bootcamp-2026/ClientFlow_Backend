package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class PrescriptionDetailsResponse {

    private Long id;

    private String generalInstructions;

    private LocalDate followUpDate;

    private String followUpNotes;

    private List<MedicineResponse> medicines;
}