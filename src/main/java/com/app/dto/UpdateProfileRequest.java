package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[+]?[0-9 ]{10,15}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;

    @Size(max = 100, message = "Specialization cannot exceed 100 characters")
    private String specialization;
}