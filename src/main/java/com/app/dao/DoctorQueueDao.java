package com.app.dao;

import com.app.entity.Appointment;
import com.app.entity.Consultation;
import com.app.entity.Prescription;
import com.app.entity.PrescriptionMedicine;
import com.app.enums.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorQueueDao {

        long countByDoctorAndStatus(
                        Long doctorId,
                        LocalDate date,
                        List<AppointmentStatus> statuses);

        long countByDoctorAndDate(
                        Long doctorId,
                        LocalDate date);

        Optional<Appointment> getCurrentPatient(
                        Long doctorId,
                        LocalDate date);

        List<Appointment> getWaitingPatients(
                        Long doctorId,
                        LocalDate date);

        Appointment getAppointmentById(Long appointmentId);

        boolean existsInProgressAppointment(Long doctorId);

        Appointment save(Appointment appointment);

        Consultation getOrCreateConsultation(Appointment appointment);

        Prescription getOrCreatePrescription(Consultation consultation);

        List<PrescriptionMedicine> getMedicines(Long prescriptionId);

        Consultation getConsultationByAppointmentId(Long appointmentId);

        Prescription getPrescriptionByConsultationId(Long consultationId);

        void saveMedicines(List<PrescriptionMedicine> medicines);
}