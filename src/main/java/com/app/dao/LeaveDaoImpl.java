package com.app.dao;

import com.app.entity.LeaveException;
import com.app.repository.LeaveRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LeaveDaoImpl implements LeaveDao{

    private final LeaveRepository leaveRepository;

    public boolean existsLeave(Long scheduleId,LocalDate date) {

        return leaveRepository.existsByDoctorScheduleIdAndExceptionDate(scheduleId,date);
    }

    public long countLeavesByDate(LocalDate date) {

        return leaveRepository.countByExceptionDate(date);
    }

    public List<LeaveException> getDoctorLeavesBetween(Long doctorId,LocalDate startDate,LocalDate endDate) {

        return leaveRepository
                .findByDoctorScheduleDoctorIdAndExceptionDateBetween(doctorId,startDate,endDate);
    }
    public boolean existsByScheduleAndDate(Long scheduleId,LocalDate date) {

    return leaveRepository
            .existsByDoctorScheduleIdAndExceptionDate(scheduleId,date);
}

public LeaveException save(LeaveException leave) {
    return leaveRepository.save(leave);
}
}