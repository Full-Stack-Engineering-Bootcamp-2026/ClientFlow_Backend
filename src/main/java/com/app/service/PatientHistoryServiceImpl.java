package com.app.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dao.PatientDao;
import com.app.dao.PatientHistoryDao;
import com.app.dto.ConsultationDetailsResponse;
import com.app.dto.ConsultationHistoryDetailsResponse;
import com.app.dto.MedicineResponse;
import com.app.dto.PatientHistoryPatientResponse;
import com.app.dto.PatientHistoryResponse;
import com.app.dto.PrescriptionDetailsResponse;
import com.app.dto.VisitHistoryResponse;
import com.app.entity.Appointment;
import com.app.entity.Consultation;
import com.app.entity.Patient;
import com.app.entity.Prescription;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.PrescriptionMedicineRepository;
import com.app.repository.PrescriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientHistoryServiceImpl implements PatientHistoryService {

        private final PatientHistoryDao patientHistoryDao;
        private final PatientDao patientDao;

        private final PrescriptionRepository prescriptionRepository;

        private final PrescriptionMedicineRepository prescriptionMedicineRepository;

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

        @Override
        public ConsultationHistoryDetailsResponse getConsultationDetails(
                        Long consultationId) {

                Consultation consultation = patientHistoryDao.getConsultationById(
                                consultationId);

                Appointment appointment = consultation.getAppointment();

                Patient patient = appointment.getPatient();

                Prescription prescription = prescriptionRepository
                                .findByConsultationId(consultation.getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Prescription not found"));

                List<MedicineResponse> medicines = prescriptionMedicineRepository
                                .findByPrescriptionId(prescription.getId())
                                .stream()
                                .map(medicine -> MedicineResponse.builder()
                                                .medicineName(medicine.getMedicineName())
                                                .medicineCategory(medicine.getMedicineCategory())
                                                .medicineUnit(medicine.getMedicineUnit())
                                                .dosage(medicine.getDosage())
                                                .frequency(medicine.getFrequency())
                                                .durationDays(medicine.getDurationDays())
                                                .instructions(medicine.getInstructions())
                                                .build())
                                .toList();

                return ConsultationHistoryDetailsResponse.builder()
                                .consultationId(consultation.getId())
                                .appointmentDate(
                                                appointment.getAppointmentDate())
                                .doctorName(
                                                appointment.getDoctor().getFullName())
                                .patient(
                                                PatientHistoryPatientResponse.builder()
                                                                .id(patient.getId())
                                                                .fullName(patient.getFullName())
                                                                .gender(patient.getGender().name())
                                                                .age(calculateAge(patient))
                                                                .bloodGroup(patient.getBloodGroup())
                                                                .build())
                                .consultation(
                                                ConsultationDetailsResponse.builder()
                                                                .id(consultation.getId())
                                                                .diagnosis(consultation.getDiagnosis())
                                                                .clinicalNotes(
                                                                                consultation.getClinicalNotes())
                                                                .build())
                                .prescription(
                                                PrescriptionDetailsResponse.builder()
                                                                .id(prescription.getId())
                                                                .generalInstructions(
                                                                                prescription.getGeneralInstructions())
                                                                .followUpDate(
                                                                                prescription.getFollowUpDate())
                                                                .followUpNotes(
                                                                                prescription.getFollowUpNotes())
                                                                .medicines(medicines)
                                                                .build())
                                .build();
        }

        private Integer calculateAge(Patient patient) {

                if (patient.getDateOfBirth() == null) {
                        return null;
                }

                return Period.between(
                                patient.getDateOfBirth(),
                                LocalDate.now()).getYears();
        }
}