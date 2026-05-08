package com.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.DoctorScheduleDao;
import com.app.dao.LeaveDao;
import com.app.dto.LeaveRequest;
import com.app.entity.DoctorSchedule;
import com.app.entity.LeaveException;
import com.app.enums.DayOfWeek;
import com.app.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService{

    private final LeaveDao leaveDao;
    private final DoctorScheduleDao scheduleDao;

    @Transactional
    public void createLeave(LeaveRequest request) {

        DayOfWeek day = DayOfWeek.valueOf(
                request.getDate()
                        .getDayOfWeek()
                        .name()
                        .substring(0, 3)
        );

        DoctorSchedule schedule =
                scheduleDao.getDoctorSchedule(
                        request.getDoctorId(),
                        day
                );

        boolean exists =
                leaveDao.existsByScheduleAndDate(
                        schedule.getId(),
                        request.getDate()
                );

        if (exists) {
            throw new BadRequestException(
                    "Leave already exists");
        }

        LeaveException leave = LeaveException.builder()
                .doctorSchedule(schedule)
                .exceptionDate(request.getDate())
                .reason(request.getReason())
                .build();

        leaveDao.save(leave);
    }
}