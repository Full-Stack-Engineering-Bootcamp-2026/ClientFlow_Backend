package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DoctorDashboardResponse {

    private DoctorQueueStatsResponse stats;

    private CurrentPatientResponse currentPatient;

    private List<WaitingPatientResponse> waitingPatients;

    private List<CompletedConsultationResponse> completedConsultations;
}