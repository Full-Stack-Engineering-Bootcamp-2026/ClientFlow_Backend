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


public interface LeaveService {

 void createLeave(LeaveRequest request) ;
}