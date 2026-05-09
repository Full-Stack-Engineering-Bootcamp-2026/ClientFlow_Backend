package com.app.service.patient.impl;

import com.app.entity.Appointment;
import com.app.entity.Staff;
import com.app.enums.AppointmentStatus;
import com.app.enums.VisitType;
import com.app.dao.patient.LiveQueueDao;
import com.app.dto.patient.response.*;
import com.app.service.patient.LiveQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiveQueueServiceImpl implements LiveQueueService {

    private final LiveQueueDao liveQueueDao;

    @Override
    public LiveQueueDashboardResponse getLiveQueue() {

        List<Appointment> appointments =
                liveQueueDao.getTodayAppointments(LocalDate.now());

        long totalWaiting = appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.WAITING)
                .count();

        long urgentCases = appointments.stream()
                .filter(a -> a.getVisitType() == VisitType.URGENT)
                .filter(a -> a.getStatus() == AppointmentStatus.WAITING)
                .count();

        Map<Staff, List<Appointment>> doctorMap = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getDoctor));

        List<DoctorQueueCardResponse> doctorCards = new ArrayList<>();

        for (Map.Entry<Staff, List<Appointment>> entry : doctorMap.entrySet()) {

            Staff doctor = entry.getKey();

            List<Appointment> doctorAppointments = entry.getValue()
                    .stream()
                    .sorted(Comparator.comparing(Appointment::getQueueNumber))
                    .toList();

            QueuePatientResponse servingNow = null;
            QueuePatientResponse nextUp = null;
            QueuePatientResponse lastServed = null;

            List<QueuePatientResponse> waitingPatients =
                    doctorAppointments.stream()
                            .filter(a -> a.getStatus() == AppointmentStatus.WAITING)
                            .map(this::mapPatient)
                            .toList();

            Optional<Appointment> inProgress = doctorAppointments.stream()
                    .filter(a -> a.getStatus() == AppointmentStatus.IN_PROGRESS)
                    .findFirst();

            Optional<Appointment> completed = doctorAppointments.stream()
                    .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                    .max(Comparator.comparing(Appointment::getQueueNumber));
            if (inProgress.isPresent()) {

                servingNow = mapPatient(inProgress.get());

                nextUp = doctorAppointments.stream()
                        .filter(a -> a.getStatus() == AppointmentStatus.WAITING)
                        .findFirst()
                        .map(this::mapPatient)
                        .orElse(null);
            }

            if (inProgress.isEmpty() && completed.isPresent()) {

                lastServed = mapPatient(completed.get());

                nextUp = doctorAppointments.stream()
                        .filter(a -> a.getStatus() == AppointmentStatus.WAITING)
                        .findFirst()
                        .map(this::mapPatient)
                        .orElse(null);
            }
            String queueState;

            boolean hasWaiting = waitingPatients.size() > 0;
            boolean hasInProgress = inProgress.isPresent();
            boolean hasCompleted = completed.isPresent();

            if (hasInProgress) {
                queueState = "ACTIVE";
            } else if (hasWaiting && !hasCompleted) {
                queueState = "YET_TO_START";
            } else if (hasWaiting && hasCompleted) {
                queueState = "BETWEEN_CONSULTATIONS";
            } else {
                queueState = "BREAK";
            }

            doctorCards.add(
                    DoctorQueueCardResponse.builder()
                            .doctorId(doctor.getId())
                            .doctorName(doctor.getFullName())
                            .specialization(doctor.getSpecialization())
                            .queueState(queueState)
                            .servingNow(servingNow)
                            .nextUp(nextUp)
                            .lastServed(lastServed)
                            .waitingCount(waitingPatients.size())
                            .waitingPatients(waitingPatients)
                            .build()
            );
        }
        QueueStatsResponse stats = QueueStatsResponse.builder()
                .totalWaitingPatients(totalWaiting)
                .activeDoctors((long) doctorCards.size())
                .urgentCases(urgentCases)
                .averageWaitTime(18L)
                .build();

        return LiveQueueDashboardResponse.builder()
                .stats(stats)
                .doctorQueues(doctorCards)
                .build();
    }

    private QueuePatientResponse mapPatient(Appointment appointment) {

        return QueuePatientResponse.builder()
                .appointmentId(appointment.getId())
                .queueNumber(appointment.getQueueNumber())
                .patientName(appointment.getPatient().getFullName())
                .mobile(appointment.getPatient().getMobile())
                .status(appointment.getStatus().name())
                .build();
    }
    }