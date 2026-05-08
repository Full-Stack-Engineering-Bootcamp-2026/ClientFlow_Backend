package com.app.service;


import com.app.dto.DoctorScheduleDashboardResponse;


import java.time.LocalDate;

import java.util.List;


public interface DashboardService {

    public List<DoctorScheduleDashboardResponse>getDoctorScheduleDashboard(LocalDate startDate) ;
    
}