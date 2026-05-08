package com.app.service;

import com.app.dao.DoctorQueueDao;
import com.app.dao.ProfileDao;
import com.app.dto.CallNextPatientRequest;
import com.app.dto.CallNextPatientResponse;
import com.app.dto.CurrentPatientResponse;
import com.app.dto.DoctorDashboardResponse;
import com.app.dto.DoctorQueueStatsResponse;
import com.app.dto.WaitingPatientResponse;
import com.app.entity.Appointment;
import com.app.entity.Patient;
import com.app.entity.Staff;
import com.app.enums.AppointmentStatus;
import com.app.exception.BadRequestException;
import com.app.exception.ResourceNotFoundException;
import com.app.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
}