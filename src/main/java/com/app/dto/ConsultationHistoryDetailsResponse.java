package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ConsultationHistoryDetailsResponse {

    private Long consultationId;

    private LocalDate appointmentDate;

    private String doctorName;

    private PatientHistoryPatientResponse patient;

    private ConsultationDetailsResponse consultation;

    private PrescriptionDetailsResponse prescription;
}