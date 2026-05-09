package com.app.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.app.entity.Consultation;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.ConsultationRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PatientHistoryDaoImpl implements PatientHistoryDao {

        private final ConsultationRepository consultationRepository;

        @Override
        public List<Consultation> getPatientConsultationHistory(
                        Long patientId) {

                return consultationRepository
                                .findByAppointmentPatientIdOrderByCreatedAtDesc(
                                                patientId);
        }

        @Override
        public Consultation getConsultationById(
                        Long consultationId) {

                return consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Consultation not found"));
        }
}