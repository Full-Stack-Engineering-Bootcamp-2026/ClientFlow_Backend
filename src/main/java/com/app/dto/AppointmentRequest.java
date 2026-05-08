package com.app.dto;

import com.app.enums.Gender;
import com.app.enums.VisitType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "Doctor id is required")
    private Long doctorId;

    private Long patientId;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date cannot be in past")
    private LocalDate appointmentDate;

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotBlank(message = "Patient phone is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Invalid mobile number"
    )
    private String patientPhone;

    private Gender gender;

    @NotNull(message = "Visit type is required")
    private VisitType visitType;

    private String notes;
}