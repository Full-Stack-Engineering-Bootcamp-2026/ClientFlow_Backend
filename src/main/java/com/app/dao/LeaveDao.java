package com.app.dao;

import com.app.entity.LeaveException;


import java.time.LocalDate;
import java.util.List;


public interface LeaveDao {


    public boolean existsLeave(Long scheduleId,LocalDate date) ;

    public long countLeavesByDate(LocalDate date) ;

    public List<LeaveException> getDoctorLeavesBetween(Long doctorId,LocalDate startDate,LocalDate endDate) ;
    public boolean existsByScheduleAndDate(Long scheduleId,LocalDate date) ;

    public LeaveException save(LeaveException leave) ;
}