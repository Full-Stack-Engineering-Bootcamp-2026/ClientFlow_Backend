package com.app.service;

import com.app.dao.AppointmentDao;
import com.app.dao.DoctorScheduleDao;
import com.app.dao.LeaveDao;
import com.app.dto.DoctorScheduleDashboardResponse;
import com.app.enums.DayOfWeek;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

    private final DoctorScheduleDao scheduleDao;
    private final LeaveDao leaveDao;
    private final AppointmentDao appointmentDao;

    public List<DoctorScheduleDashboardResponse>
    getDoctorScheduleDashboard(LocalDate startDate) {

        List<DoctorScheduleDashboardResponse> response =
                new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            LocalDate currentDate = startDate.plusDays(i);

            DayOfWeek day = DayOfWeek.valueOf(
                    currentDate.getDayOfWeek()
                            .name()
                            .substring(0, 3)
            );

            long scheduledDoctors =
                    scheduleDao.countActiveDoctorsByDay(day);

            long leaves =
                    leaveDao.countLeavesByDate(currentDate);

            long workingDoctors =
                    Math.max(0, scheduledDoctors - leaves);

            long appointments =
                    appointmentDao.countAppointmentsByDate(currentDate);

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