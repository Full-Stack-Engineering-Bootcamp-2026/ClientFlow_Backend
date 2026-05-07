package com.app.service.patient.impl;

import com.app.dao.patient.AppointmentDao;
import com.app.dto.patient.request.BookAppointmentRequest;
import com.app.dto.patient.response.AppointmentBookingResponse;
import com.app.dto.patient.response.DoctorDropdownResponse;
import com.app.dto.patient.response.PatientSearchResponse;
import com.app.entity.*;
import com.app.enums.AppointmentStatus;
import com.app.enums.DayOfWeek;
import com.app.exception.BadRequestException;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.*;
import com.app.service.patient.AppointmentService;
import com.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
        private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentDao appointmentDao;

    private final StaffRepository staffRepository;

    private final DoctorScheduleRepository doctorScheduleRepository;

    private final LeaveExceptionRepository leaveExceptionRepository;

    private final SecurityUtil securityUtil;

    @Override
public List<DoctorDropdownResponse> getAllDoctors() {

    return staffRepository.findAllActiveDoctors()
            .stream()
            .map(doctor ->
                    DoctorDropdownResponse.builder()
                            .doctorId(doctor.getId())
                            .fullName(doctor.getFullName())
                            .specialization(doctor.getSpecialization())
                            .build()
            )
            .toList();
}

    @Override
    public List<PatientSearchResponse> searchPatients(String keyword) {

        List<Patient> patients = patientRepository
                .findByFullNameContainingIgnoreCase(keyword);

        return patients.stream()
                .map(patient -> {

                    boolean hasHistory = appointmentRepository
                            .existsByPatientId(patient.getId());

                    return PatientSearchResponse.builder()
                            .patientId(patient.getId())
                            .fullName(patient.getFullName())
                            .mobile(patient.getMobile())
                            .hasHistory(hasHistory)
                            .patientType(hasHistory ? "REGULAR" : "NEW")
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional
    public AppointmentBookingResponse bookAppointment(BookAppointmentRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found")
                );

        Staff doctor = staffRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found")
                );

        if (!doctor.getIsActive()) {
            throw new BadRequestException("Doctor is inactive");
        }
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(
                request.getAppointmentDate()
                        .getDayOfWeek()
                        .name()
                        .substring(0,3)
        );

        DoctorSchedule doctorSchedule = doctorScheduleRepository
                .findByDoctorIdAndDayOfWeekAndIsActiveTrue(
                        doctor.getId(),
                        dayOfWeek
                )
                .orElseThrow(() ->
                        new BadRequestException(
                                "Doctor is unavailable on selected date"
                        )
                );

                boolean onLeave = leaveExceptionRepository
                .existsByDoctorScheduleDoctorIdAndExceptionDate(
                        doctor.getId(),
                        request.getAppointmentDate()
                );

        if (onLeave) {
            throw new BadRequestException(
                    "Doctor is on leave for selected date"
            );
        }
        long existingAppointments = appointmentRepository
                .countByDoctorIdAndAppointmentDateAndStatusIn(
                        doctor.getId(),
                        request.getAppointmentDate(),
                        List.of(
                                AppointmentStatus.WAITING,
                                AppointmentStatus.IN_PROGRESS
                        )
                );

        if (existingAppointments >= doctorSchedule.getMaxAppointments()) {
            throw new BadRequestException(
                    "Doctor appointment limit reached for selected date"
            );
        }
        Integer maxQueueNumber = appointmentRepository
                .findMaxQueueNumber(
                        doctor.getId(),
                        request.getAppointmentDate()
                );

        int nextQueueNumber = maxQueueNumber + 1;

        String loggedInEmail = securityUtil.getCurrentUserEmail();

        Staff bookedBy = staffRepository.findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logged in staff not found")
                );

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .bookedBy(bookedBy)
                .appointmentDate(request.getAppointmentDate())
                .queueNumber(nextQueueNumber)
                .visitType(request.getVisitType())
                .status(AppointmentStatus.WAITING)
                .notes(request.getNotes())
                .build();
        Appointment savedAppointment = appointmentDao.save(appointment);

        return AppointmentBookingResponse.builder()
                .appointmentId(savedAppointment.getId())
                .patientName(patient.getFullName())
                .doctorName(doctor.getFullName())
                .appointmentDate(savedAppointment.getAppointmentDate())
                .queueNumber(savedAppointment.getQueueNumber())
                .queueLabel("Q-" + savedAppointment.getQueueNumber())
                .status(savedAppointment.getStatus().name())
                .build();
    }


    
}