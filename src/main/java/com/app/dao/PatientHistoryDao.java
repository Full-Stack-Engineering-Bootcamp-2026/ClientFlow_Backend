package com.app.dao;

import java.util.List;

import com.app.entity.Consultation;

public interface PatientHistoryDao {

    List<Consultation> getPatientConsultationHistory(Long patientId);

    Consultation getConsultationById(Long consultationId);
}