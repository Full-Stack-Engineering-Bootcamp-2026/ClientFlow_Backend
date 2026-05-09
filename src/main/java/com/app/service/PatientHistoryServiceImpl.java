package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dao.PatientDao;
import com.app.dao.PatientHistoryDao;
import com.app.dto.PatientHistoryPatientResponse;
import com.app.dto.PatientHistoryResponse;
import com.app.dto.VisitHistoryResponse;
import com.app.entity.Appointment;
import com.app.entity.Patient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientHistoryServiceImpl implements PatientHistoryService {

    private final PatientHistoryDao patientHistoryDao;
    private final PatientDao patientDao;

    @Override
    public PatientHistoryResponse getPatientHistory(
            Long patientId) {

        Patient patient = patientDao.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<VisitHistoryResponse> history = patientHistoryDao.getPatientConsultationHistory(patientId)
                .stream()
                .map(consultation -> {

                    Appointment appointment = consultation.getAppointment();

                    return VisitHistoryResponse.builder()
                            .consultationId(
                                    consultation.getId())
                            .appointmentDate(
                                    appointment.getAppointmentDate())
                            .diagnosis(
                                    consultation.getDiagnosis())
                            .doctorName(
                                    appointment.getDoctor()
                                            .getFullName())
                            .status(
                                    appointment.getStatus().name())
                            .build();
                })
                .toList();

        return PatientHistoryResponse.builder()
                .patient(
                        PatientHistoryPatientResponse.builder()
                                .id(patient.getId())
                                .fullName(patient.getFullName())
                                .gender(patient.getGender().name())
                                .bloodGroup(patient.getBloodGroup())
                                .build())
                .visitHistory(history)
                .build();
    }
}