package com.app.service;

import com.app.config.*;
import com.app.dao.*;
import com.app.dto.AppointmentRequest;
import com.app.dto.AppointmentResponse;
import com.app.entity.*;
import com.app.enums.AppointmentStatus;
import com.app.enums.DayOfWeek;
import com.app.exception.BadRequestException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

        private final AppointmentDao appointmentDao;
        private final StaffDao staffDao;
        private final PatientDao patientDao;
        private final DoctorScheduleDao scheduleDao;
        private final LeaveDao leaveDao;

        @Transactional
        public AppointmentResponse bookAppointment(AppointmentRequest request) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                String email = authentication.getName();

                Staff bookedBy = staffDao.getByEmail(email);

                Staff doctor = staffDao.getById(request.getDoctorId());

                if (!doctor.getRole().getName().equals("DOCTOR")) {
                        throw new BadRequestException("Invalid doctor");
                }

                if (!doctor.getIsActive()) {
                        throw new BadRequestException("Doctor is inactive");
                }

                Patient patient = patientDao
                                .findByMobile(request.getPatientPhone())
                                .orElseGet(() -> {

                                        Patient p = Patient.builder()
                                                        .fullName(request.getPatientName())
                                                        .mobile(request.getPatientPhone())
                                                        .registeredBy(bookedBy)
                                                        .gender(request.getGender())
                                                        .build();

                                        return patientDao.save(p);
                                });

                boolean alreadyBooked = appointmentDao.existsPatientBooking(
                                doctor.getId(),
                                patient.getId(),
                                request.getAppointmentDate());

                if (alreadyBooked) {
                        throw new BadRequestException(
                                        "Patient already booked for this doctor today");
                }

                DayOfWeek day = DayOfWeek.valueOf(
                                request.getAppointmentDate()
                                                .getDayOfWeek()
                                                .name()
                                                .substring(0, 3));

                DoctorSchedule schedule = scheduleDao.getDoctorSchedule(
                                doctor.getId(),
                                day);

                if (!schedule.getIsActive()) {
                        throw new BadRequestException("Schedule inactive");
                }

                long count = appointmentDao.countDoctorAppointments(
                                doctor.getId(),
                                request.getAppointmentDate());

                if (count >= schedule.getMaxAppointments()) {
                        throw new BadRequestException("Slots full");
                }

                boolean onLeave = leaveDao.existsLeave(
                                schedule.getId(),
                                request.getAppointmentDate());

                if (onLeave) {
                        throw new BadRequestException(
                                        "Doctor is on leave on this date");
                }

                int nextQueue = appointmentDao.getNextQueueNumber(
                                doctor.getId(),
                                request.getAppointmentDate());

                Appointment appointment = Appointment.builder()
                                .doctor(doctor)
                                .patient(patient)
                                .bookedBy(bookedBy)
                                .appointmentDate(request.getAppointmentDate())
                                .queueNumber(nextQueue)
                                .status(AppointmentStatus.WAITING)
                                .notes(request.getNotes())
                                .build();

                appointmentDao.save(appointment);

                return new AppointmentResponse(
                                appointment.getId(),
                                doctor.getFullName(),
                                appointment.getQueueNumber(),
                                appointment.getStatus().name(),
                                appointment.getAppointmentDate());
        }
}