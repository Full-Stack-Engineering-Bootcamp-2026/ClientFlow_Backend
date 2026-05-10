package com.app.service;

import com.app.dto.ConsultationHistoryDetailsResponse;
import com.app.dto.PatientHistoryResponse;

public interface PatientHistoryService {

    PatientHistoryResponse getPatientHistory(Long patientId);

    ConsultationHistoryDetailsResponse getConsultationDetails(Long consultationId);
}