package com.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.LeaveRequest;
import com.app.entity.DoctorSchedule;
import com.app.entity.LeaveException;
import com.app.exception.BadRequestException;
import com.app.repository.DoctorScheduleRepository;
import com.app.repository.LeaveRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;

    private final DoctorScheduleRepository scheduleRepository;

    @Transactional
public void createLeave(LeaveRequest request) {

    com.app.enums.DayOfWeek day = com.app.enums.DayOfWeek.valueOf(
            request.getDate().getDayOfWeek().name().substring(0, 3)
    );

    DoctorSchedule schedule = scheduleRepository
            .findByDoctorIdAndDayOfWeek(request.getDoctorId(), day)
            .orElseThrow(() -> new BadRequestException("Doctor not scheduled on this day"));

    boolean exists = leaveRepository
            .existsByDoctorScheduleIdAndExceptionDate(
                    schedule.getId(),
                    request.getDate()
            );

    if (exists) {
        throw new BadRequestException("Leave already exists");
    }

    leaveRepository.save(
            LeaveException.builder()
                    .doctorSchedule(schedule)
                    .exceptionDate(request.getDate())
                    .reason(request.getReason())
                    .build()
    );
}

}