package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompletedConsultationResponse {

    private Long consultationId;

    private Long appointmentId;

    private Integer queueNumber;

    private String patientName;

    private String completedAt;

    private String diagnosis;
}