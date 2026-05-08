package com.app.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

import com.app.enums.Gender;

@Data
public class AppointmentRequest {

     @NotNull
    private Long doctorId;

    @NotNull
    private LocalDate appointmentDate;

    @NotBlank
    private String patientName;

    @NotBlank
    private String patientPhone;

    @NotNull
    private Gender gender;

    private String notes;
}