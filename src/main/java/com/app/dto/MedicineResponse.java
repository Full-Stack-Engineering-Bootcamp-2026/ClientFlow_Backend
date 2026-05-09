package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MedicineResponse {

    private String medicineName;

    private String medicineCategory;

    private String medicineUnit;

    private String dosage;

    private String frequency;

    private Integer durationDays;

    private String instructions;
}