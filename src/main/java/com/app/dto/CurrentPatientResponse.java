package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CurrentPatientResponse {

    private Long appointmentId;

    private Integer queueNumber;

    private String patientName;

    private String gender;

    private Integer age;

    private String reason;
}