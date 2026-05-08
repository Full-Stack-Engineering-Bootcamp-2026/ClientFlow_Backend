package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CallNextPatientResponse {

    private String message;

    private Long appointmentId;
}