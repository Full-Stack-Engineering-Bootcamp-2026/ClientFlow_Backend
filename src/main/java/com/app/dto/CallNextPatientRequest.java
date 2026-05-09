package com.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallNextPatientRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
}