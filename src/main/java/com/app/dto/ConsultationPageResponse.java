package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConsultationPageResponse {

    private Long appointmentId;

    private Integer queueNumber;

    private ConsultationPatientResponse patient;

    private ConsultationDetailsResponse consultation;

    private PrescriptionDetailsResponse prescription;
}