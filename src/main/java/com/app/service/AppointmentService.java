package com.app.service;

import com.app.dto.AppointmentRequest;
import com.app.dto.AppointmentResponse;
import com.app.entity.*;
import com.app.enums.AppointmentStatus;
import com.app.enums.DayOfWeek;
import com.app.exception.BadRequestException;
import com.app.exception.ResourceNotFoundException;
import com.app.exception.UnauthorizedException;
import com.app.repository.*;
import com.app.config.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final LeaveRepository leaveRepository;

    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Staff bookedBy = staffRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        Staff doctor = staffRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (!doctor.getRole().getName().equals("DOCTOR")) {
            throw new BadRequestException("Invalid doctor");
        }

        if (!doctor.getIsActive()) {
            throw new BadRequestException("Doctor is inactive");
        }

        Patient patient = patientRepository.findByMobile(request.getPatientPhone())
                .orElseGet(() -> {
                    Patient p = Patient.builder()
                            .fullName(request.getPatientName())
                            .mobile(request.getPatientPhone())
                            .registeredBy(bookedBy)
                            .gender(request.getGender())
                            .build();
                    return patientRepository.save(p);
                });

        if (appointmentRepository.existsByDoctorIdAndPatientIdAndAppointmentDate(
                doctor.getId(),
                patient.getId(),
                request.getAppointmentDate())) {
            throw new BadRequestException("Patient already booked for this doctor today");
        }

        DayOfWeek day = DayOfWeek.valueOf(
                request.getAppointmentDate().getDayOfWeek().name().substring(0, 3));

        DoctorSchedule schedule = scheduleRepository
                .findByDoctorIdAndDayOfWeek(doctor.getId(), day)
                .orElseThrow(() -> new BadRequestException("Doctor not available on this day"));

        if (!schedule.getIsActive()) {
            throw new BadRequestException("Schedule inactive");
        }

        long count = appointmentRepository
                .countByDoctorIdAndAppointmentDate(
                        doctor.getId(),
                        request.getAppointmentDate());

        if (count >= schedule.getMaxAppointments()) {
            throw new BadRequestException("Slots full");
        }

        boolean onLeave = leaveRepository.existsByDoctorScheduleIdAndExceptionDate(
                schedule.getId(),
                request.getAppointmentDate());

        if (onLeave) {
            throw new BadRequestException("Doctor is on leave on this date");
        }

        int nextQueue = appointmentRepository
                .findTopByDoctorIdAndAppointmentDateOrderByQueueNumberDesc(
                        doctor.getId(),
                        request.getAppointmentDate())
                .map(a -> a.getQueueNumber() + 1)
                .orElse(1);

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .bookedBy(bookedBy)
                .appointmentDate(request.getAppointmentDate())
                .queueNumber(nextQueue)
                .status(AppointmentStatus.WAITING)
                .notes(request.getNotes())
                .build();

        appointmentRepository.save(appointment);

        return new AppointmentResponse(
                appointment.getId(),
                doctor.getFullName(),
                appointment.getQueueNumber(),
                appointment.getStatus().name(),
                appointment.getAppointmentDate());

    }

}