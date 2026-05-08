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


public interface DashboardService {

    public List<DoctorScheduleDashboardResponse>getDoctorScheduleDashboard(LocalDate startDate) ;
    
}