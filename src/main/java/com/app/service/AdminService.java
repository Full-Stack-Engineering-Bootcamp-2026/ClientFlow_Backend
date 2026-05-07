package com.app.service;

import org.springframework.stereotype.Service;

import com.app.dto.DashboardSummaryResponse;
import com.app.enums.AppointmentStatus;
import com.app.repository.AppointmentRepository;
import com.app.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {


     private final StaffRepository staffRepository;
    private final AppointmentRepository appointmentRepository;


     public DashboardSummaryResponse getDashboardSummary() {

        long totalStaff = staffRepository.count();

        long activeDoctors = staffRepository
                .countByRole_NameAndIsActiveTrue("DOCTOR");

        long totalAppointments = appointmentRepository.count();

        long completed = appointmentRepository
                .countByStatus(AppointmentStatus.COMPLETED);

        long cancelled = appointmentRepository
                .countByStatus(AppointmentStatus.CANCELLED);

        return new DashboardSummaryResponse(
                totalStaff,
                activeDoctors,
                totalAppointments,
                completed,
                cancelled
        );
    }

    
}