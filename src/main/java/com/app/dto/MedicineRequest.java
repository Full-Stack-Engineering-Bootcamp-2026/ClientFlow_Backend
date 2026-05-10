package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicineRequest {

    @NotBlank
    private String medicineName;

    private String medicineCategory;

    private String medicineUnit;

    @NotBlank
    private String dosage;

    @NotBlank
    private String frequency;

    @NotNull
    private Integer durationDays;

    private String instructions;
}