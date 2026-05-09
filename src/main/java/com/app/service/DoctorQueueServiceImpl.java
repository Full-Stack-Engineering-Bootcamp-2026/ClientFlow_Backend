package com.app.service;

import com.app.dao.DoctorQueueDao;
import com.app.dao.ProfileDao;
import com.app.dto.CallNextPatientRequest;
import com.app.dto.CallNextPatientResponse;
import com.app.dto.CompleteConsultationRequest;
import com.app.dto.CompletedConsultationResponse;
import com.app.dto.ConsultationDetailsResponse;
import com.app.dto.ConsultationPageResponse;
import com.app.dto.ConsultationPatientResponse;
import com.app.dto.CurrentPatientResponse;
import com.app.dto.DoctorDashboardResponse;
import com.app.dto.DoctorQueueStatsResponse;
import com.app.dto.MedicineResponse;
import com.app.dto.PrescriptionDetailsResponse;
import com.app.dto.WaitingPatientResponse;
import com.app.entity.Appointment;
import com.app.entity.Consultation;
import com.app.entity.Patient;
import com.app.entity.Prescription;
import com.app.entity.PrescriptionMedicine;
import com.app.entity.Staff;
import com.app.enums.AppointmentStatus;
import com.app.exception.BadRequestException;
import com.app.exception.ResourceNotFoundException;
import com.app.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorQueueServiceImpl implements DoctorQueueService {

        private final DoctorQueueDao doctorQueueDao;
        private final ProfileDao profileDao;

        @Override
        public DoctorDashboardResponse getDashboard() {

                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                Staff doctor = profileDao.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                LocalDate today = LocalDate.now();

                long waiting = doctorQueueDao.countByDoctorAndStatus(
                                doctor.getId(),
                                today,
                                List.of(AppointmentStatus.WAITING));

                long inProgress = doctorQueueDao.countByDoctorAndStatus(
                                doctor.getId(),
                                today,
                                List.of(AppointmentStatus.IN_PROGRESS));

                long completed = doctorQueueDao.countByDoctorAndStatus(
                                doctor.getId(),
                                today,
                                List.of(AppointmentStatus.COMPLETED));

                long totalToday = doctorQueueDao.countByDoctorAndDate(
                                doctor.getId(),
                                today);

                CurrentPatientResponse currentPatient = doctorQueueDao
                                .getCurrentPatient(doctor.getId(), today)
                                .map(this::mapCurrentPatient)
                                .orElse(null);

                List<WaitingPatientResponse> waitingPatients = doctorQueueDao.getWaitingPatients(doctor.getId(), today)
                                .stream()
                                .map(this::mapWaitingPatient)
                                .toList();

                List<CompletedConsultationResponse> completedConsultations = doctorQueueDao
                                .getCompletedConsultations(
                                                doctor.getId())
                                .stream()
                                .map(consultation -> {

                                        Appointment appointment = consultation.getAppointment();

                                        return CompletedConsultationResponse
                                                        .builder()
                                                        .consultationId(
                                                                        consultation.getId())
                                                        .appointmentId(
                                                                        appointment.getId())
                                                        .queueNumber(
                                                                        appointment.getQueueNumber())
                                                        .patientName(
                                                                        appointment.getPatient()
                                                                                        .getFullName())
                                                        .completedAt(
                                                                        consultation.getCompletedAt()
                                                                                        .format(
                                                                                                        DateTimeFormatter
                                                                                                                        .ofPattern(
                                                                                                                                        "hh:mm a")))
                                                        .diagnosis(
                                                                        consultation.getDiagnosis())
                                                        .build();
                                })
                                .toList();

                return DoctorDashboardResponse.builder()
                                .stats(
                                                DoctorQueueStatsResponse.builder()
                                                                .waiting(waiting)
                                                                .inProgress(inProgress)
                                                                .completed(completed)
                                                                .totalToday(totalToday)
                                                                .build())
                                .currentPatient(currentPatient)
                                .waitingPatients(waitingPatients)
                                .completedConsultations(
                                                completedConsultations)
                                .build();
        }

        private CurrentPatientResponse mapCurrentPatient(Appointment appointment) {

                Patient patient = appointment.getPatient();

                return CurrentPatientResponse.builder()
                                .appointmentId(appointment.getId())
                                .queueNumber(appointment.getQueueNumber())
                                .patientName(patient.getFullName())
                                .gender(patient.getGender().name())
                                .age(calculateAge(patient))
                                .reason(appointment.getNotes())
                                .build();
        }

        private WaitingPatientResponse mapWaitingPatient(Appointment appointment) {

                Patient patient = appointment.getPatient();

                return WaitingPatientResponse.builder()
                                .appointmentId(appointment.getId())
                                .queueNumber(appointment.getQueueNumber())
                                .patientName(patient.getFullName())
                                .gender(patient.getGender().name())
                                .age(calculateAge(patient))
                                .bookedAt(
                                                appointment.getCreatedAt()
                                                                .format(DateTimeFormatter.ofPattern("hh:mm a")))
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

        @Override
        public CallNextPatientResponse callNextPatient(
                        CallNextPatientRequest request) {

                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                Staff doctor = profileDao.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                Appointment appointment = doctorQueueDao
                                .getAppointmentById(request.getAppointmentId());

                if (!appointment.getDoctor().getId().equals(doctor.getId())) {
                        throw new UnauthorizedException("Appointment does not belong to doctor");
                }

                if (appointment.getStatus() != AppointmentStatus.WAITING) {
                        throw new BadRequestException(
                                        "Only waiting appointments can be moved to in progress");
                }

                if (doctorQueueDao.existsInProgressAppointment(doctor.getId())) {
                        throw new BadRequestException(
                                        "Another appointment is already in progress");
                }

                appointment.setStatus(AppointmentStatus.IN_PROGRESS);

                doctorQueueDao.save(appointment);

                return CallNextPatientResponse.builder()
                                .message("Patient moved to current queue")
                                .appointmentId(appointment.getId())
                                .build();
        }

        @Override
        public ConsultationPageResponse getConsultationPage(
                        Long appointmentId) {

                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                Staff doctor = profileDao.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                Appointment appointment = doctorQueueDao
                                .getAppointmentById(appointmentId);

                if (!appointment.getDoctor().getId().equals(doctor.getId())) {
                        throw new UnauthorizedException(
                                        "Appointment does not belong to doctor");
                }

                Consultation consultation = doctorQueueDao.getOrCreateConsultation(appointment);

                Prescription prescription = doctorQueueDao.getOrCreatePrescription(consultation);

                List<MedicineResponse> medicines = doctorQueueDao.getMedicines(prescription.getId())
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

                Patient patient = appointment.getPatient();

                return ConsultationPageResponse.builder()
                                .appointmentId(appointment.getId())
                                .queueNumber(appointment.getQueueNumber())
                                .patient(
                                                ConsultationPatientResponse.builder()
                                                                .id(patient.getId())
                                                                .name(patient.getFullName())
                                                                .gender(patient.getGender().name())
                                                                .age(calculateAge(patient))
                                                                .build())
                                .consultation(
                                                ConsultationDetailsResponse.builder()
                                                                .id(consultation.getId())
                                                                .diagnosis(consultation.getDiagnosis())
                                                                .clinicalNotes(consultation.getClinicalNotes())
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

        @Override
        public void completeConsultation(
                        CompleteConsultationRequest request) {

                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                Staff doctor = profileDao.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                Appointment appointment = doctorQueueDao
                                .getAppointmentById(request.getAppointmentId());

                if (!appointment.getDoctor().getId().equals(doctor.getId())) {
                        throw new UnauthorizedException(
                                        "Appointment does not belong to doctor");
                }

                if (appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
                        throw new BadRequestException(
                                        "Appointment is not in progress");
                }

                Consultation consultation = doctorQueueDao.getConsultationByAppointmentId(
                                appointment.getId());

                Prescription prescription = doctorQueueDao.getPrescriptionByConsultationId(
                                consultation.getId());

                if (Boolean.TRUE.equals(consultation.getIsLocked())) {
                        throw new BadRequestException(
                                        "Consultation already locked");
                }

                if (Boolean.TRUE.equals(prescription.getIsLocked())) {
                        throw new BadRequestException(
                                        "Prescription already locked");
                }

                consultation.setDiagnosis(request.getDiagnosis());
                consultation.setClinicalNotes(request.getClinicalNotes());
                consultation.setCompletedAt(LocalDateTime.now());
                consultation.setIsLocked(true);

                prescription.setGeneralInstructions(
                                request.getGeneralInstructions());

                prescription.setFollowUpDate(
                                request.getFollowUpDate());

                prescription.setFollowUpNotes(
                                request.getFollowUpNotes());

                prescription.setIsLocked(true);

                List<PrescriptionMedicine> medicines = request.getMedicines()
                                .stream()
                                .map(medicine -> PrescriptionMedicine.builder()
                                                .prescription(prescription)
                                                .medicineName(medicine.getMedicineName())
                                                .medicineCategory(medicine.getMedicineCategory())
                                                .medicineUnit(medicine.getMedicineUnit())
                                                .dosage(medicine.getDosage())
                                                .frequency(medicine.getFrequency())
                                                .durationDays(medicine.getDurationDays())
                                                .instructions(medicine.getInstructions())
                                                .build())
                                .toList();

                doctorQueueDao.saveMedicines(medicines);

                appointment.setStatus(AppointmentStatus.COMPLETED);

                doctorQueueDao.save(appointment);
        }

}