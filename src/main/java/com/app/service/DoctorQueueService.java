package com.app.service;

import com.app.dto.CallNextPatientRequest;
import com.app.dto.CallNextPatientResponse;
import com.app.dto.CompleteConsultationRequest;
import com.app.dto.ConsultationPageResponse;
import com.app.dto.DoctorDashboardResponse;

public interface DoctorQueueService {

    DoctorDashboardResponse getDashboard();

    CallNextPatientResponse callNextPatient(CallNextPatientRequest request);

    ConsultationPageResponse getConsultationPage(Long appointmentId);

    void completeConsultation(CompleteConsultationRequest request);
}