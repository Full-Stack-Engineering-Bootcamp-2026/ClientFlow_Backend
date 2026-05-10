package com.app.service;

import com.app.dao.AppointmentDao;
import com.app.dao.StaffDao;
import com.app.dto.DashboardSummaryResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final StaffDao staffDao;
    private final AppointmentDao appointmentDao;

    public DashboardSummaryResponse getDashboardSummary() {

        long totalStaff = staffDao.getTotalStaffCount();

        long activeDoctors = staffDao.getActiveDoctorCount();

        long totalAppointments =
                appointmentDao.getTotalAppointmentsCount();

        long completed =
                appointmentDao.getCompletedAppointmentsCount();

        long cancelled =
                appointmentDao.getCancelledAppointmentsCount();

        return new DashboardSummaryResponse(
                totalStaff,
                activeDoctors,
                totalAppointments,
                completed,
                cancelled
        );
    }
}