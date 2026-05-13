package com.app.service;

import com.app.dao.AppointmentDao;
import com.app.dao.PatientDao;
import com.app.dao.StaffDao;
import com.app.dto.AppointmentRequest;
import com.app.dto.AppointmentResponse;
import com.app.dto.patient.response.DoctorDropdownResponse;
import com.app.dto.patient.response.PatientSearchResponse;
import com.app.entity.Appointment;
import com.app.entity.DoctorSchedule;
import com.app.entity.Patient;
import com.app.entity.Staff;
import com.app.enums.AppointmentStatus;
import com.app.enums.DayOfWeek;
import com.app.exception.BadRequestException;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.AppointmentRepository;
import com.app.repository.DoctorScheduleRepository;
import com.app.repository.LeaveExceptionRepository;
import com.app.repository.PatientRepository;
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

        private final StaffDao staffDao;

        private final DoctorScheduleRepository doctorScheduleRepository;

        private final LeaveExceptionRepository leaveExceptionRepository;

        private final SecurityUtil securityUtil;

        private final PatientDao patientDao;

        @Override
        public List<DoctorDropdownResponse> getAllDoctors() {

                return staffDao.getAllActiveDoctors()
                                .stream()
                                .map(doctor -> DoctorDropdownResponse.builder()
                                                .doctorId(doctor.getId())
                                                .fullName(doctor.getFullName())
                                                .specialization(doctor.getSpecialization())
                                                .build())
                                .toList();
        }

        @Override
        public List<PatientSearchResponse> searchPatients(
                        String keyword) {

                List<Patient> patients = patientDao.searchPatients(keyword);

                return patients.stream()
                                .map(patient -> {

                                        boolean hasHistory = appointmentRepository.existsByPatientId(
                                                        patient.getId());

                                        return PatientSearchResponse.builder()
                                                        .patientId(patient.getId())
                                                        .fullName(patient.getFullName())
                                                        .mobile(patient.getMobile())
                                                        .hasHistory(hasHistory)
                                                        .patientType(
                                                                        hasHistory ? "REGULAR" : "NEW")
                                                        .build();
                                })
                                .toList();
        }

        @Override
        @Transactional
        public AppointmentResponse bookAppointment(
                        AppointmentRequest request) {

                Patient patient = patientRepository
                                .findById(request.getPatientId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Patient not found"));

                Staff doctor = staffDao.getById(
                                request.getDoctorId());

                if (!doctor.getRole().getName().equals("DOCTOR")) {

                        throw new BadRequestException(
                                        "Invalid doctor");
                }

                if (!doctor.getIsActive()) {

                        throw new BadRequestException(
                                        "Doctor is inactive");
                }

                DayOfWeek dayOfWeek = DayOfWeek.valueOf(
                                request.getAppointmentDate()
                                                .getDayOfWeek()
                                                .name()
                                                .substring(0, 3));

                DoctorSchedule doctorSchedule = doctorScheduleRepository
                                .findByDoctorIdAndDayOfWeekAndIsActiveTrue(
                                                doctor.getId(),
                                                dayOfWeek)
                                .orElseThrow(() -> new BadRequestException(
                                                "Doctor unavailable on selected date"));

                boolean onLeave = leaveExceptionRepository
                                .existsByDoctorScheduleDoctorIdAndExceptionDate(
                                                doctor.getId(),
                                                request.getAppointmentDate());

                if (onLeave) {

                        throw new BadRequestException(
                                        "Doctor is on leave for selected date");
                }

                long existingAppointments = appointmentRepository
                                .countByDoctorIdAndAppointmentDateAndStatusIn(
                                                doctor.getId(),
                                                request.getAppointmentDate(),
                                                List.of(
                                                                AppointmentStatus.WAITING,
                                                                AppointmentStatus.IN_PROGRESS));

                if (existingAppointments >= doctorSchedule.getMaxAppointments()) {

                        throw new BadRequestException(
                                        "Doctor appointment limit reached");
                }

                boolean alreadyBooked = appointmentRepository
                                .existsByDoctorIdAndPatientIdAndAppointmentDate(
                                                doctor.getId(),
                                                patient.getId(),
                                                request.getAppointmentDate());

                if (alreadyBooked) {

                        throw new BadRequestException(
                                        "Patient already has an appointment with this doctor on selected date");
                }

                Integer maxQueueNumber = appointmentDao.findMaxQueueNumber(
                                doctor.getId(),
                                request.getAppointmentDate());

                int nextQueueNumber = maxQueueNumber + 1;

                String loggedInEmail = securityUtil.getCurrentUserEmail();

                Staff bookedBy = staffDao.getByEmail(loggedInEmail);

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

                return AppointmentResponse.builder()
                                .appointmentId(savedAppointment.getId())
                                .patientName(patient.getFullName())
                                .doctorName(doctor.getFullName())
                                .appointmentDate(savedAppointment.getAppointmentDate())
                                .queueNumber(savedAppointment.getQueueNumber())
                                .queueLabel(
                                                "Q-" + savedAppointment.getQueueNumber())
                                .status(savedAppointment.getStatus().name())
                                .build();
        }
}