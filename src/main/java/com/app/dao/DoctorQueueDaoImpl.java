package com.app.dao;

import com.app.entity.Appointment;
import com.app.entity.Consultation;
import com.app.entity.Prescription;
import com.app.entity.PrescriptionMedicine;
import com.app.enums.AppointmentStatus;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.AppointmentRepository;
import com.app.repository.ConsultationRepository;
import com.app.repository.PrescriptionMedicineRepository;
import com.app.repository.PrescriptionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DoctorQueueDaoImpl implements DoctorQueueDao {

        private final AppointmentRepository appointmentRepository;
        private final ConsultationRepository consultationRepository;

        private final PrescriptionRepository prescriptionRepository;

        private final PrescriptionMedicineRepository prescriptionMedicineRepository;

        @Override
        public long countByDoctorAndStatus(
                        Long doctorId,
                        LocalDate date,
                        List<AppointmentStatus> statuses) {

                return appointmentRepository.countByDoctorIdAndAppointmentDateAndStatusIn(
                                doctorId,
                                date,
                                statuses);
        }

        @Override
        public long countByDoctorAndDate(
                        Long doctorId,
                        LocalDate date) {

                return appointmentRepository.countByDoctorIdAndAppointmentDate(
                                doctorId,
                                date);
        }

        @Override
        public Optional<Appointment> getCurrentPatient(
                        Long doctorId,
                        LocalDate date) {

                return appointmentRepository
                                .findFirstByDoctorIdAndAppointmentDateAndStatus(
                                                doctorId,
                                                date,
                                                AppointmentStatus.IN_PROGRESS);
        }

        @Override
        public List<Appointment> getWaitingPatients(
                        Long doctorId,
                        LocalDate date) {

                return appointmentRepository
                                .findByDoctorIdAndAppointmentDateAndStatusOrderByQueueNumberAsc(
                                                doctorId,
                                                date,
                                                AppointmentStatus.WAITING);
        }

        @Override
        public Appointment getAppointmentById(Long appointmentId) {

                return appointmentRepository.findById(appointmentId)
                                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        }

        @Override
        public boolean existsInProgressAppointment(Long doctorId) {

                return appointmentRepository
                                .findFirstByDoctorIdAndAppointmentDateAndStatus(
                                                doctorId,
                                                LocalDate.now(),
                                                AppointmentStatus.IN_PROGRESS)
                                .isPresent();
        }

        @Override
        public Appointment save(Appointment appointment) {
                return appointmentRepository.save(appointment);
        }

        @Override
        public Consultation getOrCreateConsultation(Appointment appointment) {

                return consultationRepository
                                .findByAppointmentId(appointment.getId())
                                .orElseGet(() -> {

                                        Consultation consultation = Consultation.builder()
                                                        .appointment(appointment)
                                                        .doctor(appointment.getDoctor())
                                                        .build();

                                        return consultationRepository.save(consultation);
                                });
        }

        @Override
        public Prescription getOrCreatePrescription(
                        Consultation consultation) {

                return prescriptionRepository
                                .findByConsultationId(consultation.getId())
                                .orElseGet(() -> {

                                        Prescription prescription = Prescription.builder()
                                                        .consultation(consultation)
                                                        .build();

                                        return prescriptionRepository.save(prescription);
                                });
        }

        @Override
        public List<PrescriptionMedicine> getMedicines(
                        Long prescriptionId) {

                return prescriptionMedicineRepository
                                .findByPrescriptionId(prescriptionId);
        }

        @Override
        public Consultation getConsultationByAppointmentId(
                        Long appointmentId) {

                return consultationRepository
                                .findByAppointmentId(appointmentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Consultation not found"));
        }

        @Override
        public Prescription getPrescriptionByConsultationId(
                        Long consultationId) {

                return prescriptionRepository
                                .findByConsultationId(consultationId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Prescription not found"));
        }

        @Override
        public void saveMedicines(
                        List<PrescriptionMedicine> medicines) {

                prescriptionMedicineRepository.saveAll(medicines);
        }

        @Override
        public List<Consultation> getCompletedConsultations(
                        Long doctorId) {

                return consultationRepository
                                .findByDoctorIdAndAppointmentStatusOrderByCompletedAtDesc(
                                                doctorId,
                                                AppointmentStatus.COMPLETED);
        }

}