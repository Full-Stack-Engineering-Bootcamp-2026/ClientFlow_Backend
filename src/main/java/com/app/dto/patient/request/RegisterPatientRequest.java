package com.app.dto.patient.request;

import com.app.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPatientRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Full name must contain only alphabets and spaces"
    )
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must be exactly 10 digits"
    )
    private String mobile;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Size(max = 300, message = "Address cannot exceed 300 characters")
    private String address;

    @Size(max = 2000, message = "Medical notes cannot exceed 2000 characters")
    private String medicalNotes;

    @Pattern(
            regexp = "^(A|B|AB|O)[+-]$",
            message = "Invalid blood group"
    )
    private String bloodGroup;
}