package com.app.service;


import com.app.dto.DoctorScheduleDashboardResponse;
import com.app.enums.DayOfWeek;
import com.app.repository.AppointmentRepository;
import com.app.repository.DoctorScheduleRepository;
import com.app.repository.LeaveRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DoctorScheduleRepository scheduleRepository;
    private final LeaveRepository leaveRepository;
    private final AppointmentRepository appointmentRepository;

    public List<DoctorScheduleDashboardResponse> getDoctorScheduleDashboard(
            LocalDate startDate
    ) {

        List<DoctorScheduleDashboardResponse> response = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            LocalDate currentDate = startDate.plusDays(i);

            DayOfWeek day = DayOfWeek.valueOf(
                    currentDate.getDayOfWeek()
                            .name()
                            .substring(0, 3)
            );

            long scheduledDoctors =
                    scheduleRepository.countByDayOfWeekAndIsActiveTrue(day);

            long leaves =
                    leaveRepository.countByExceptionDate(currentDate);

            long workingDoctors = Math.max(0, scheduledDoctors - leaves);

            long appointments =
                    appointmentRepository.countByAppointmentDate(currentDate);

            response.add(
                    new DoctorScheduleDashboardResponse(
                            day.name(),
                            currentDate,
                            workingDoctors,
                            appointments
                    )
            );
        }

        return response;
    }
}