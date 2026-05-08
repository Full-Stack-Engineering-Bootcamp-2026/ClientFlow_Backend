package com.app.service;

import java.time.LocalDate;
import java.util.List;

import com.app.dto.DoctorScheduleRequest;
import com.app.dto.DoctorScheduleResponse;
import com.app.dto.DoctorWeeklyScheduleRequest;
import com.app.dto.DoctorWeeklyScheduleResponse;
import com.app.dto.UpdateDoctorScheduleRequest;


public interface DoctorScheduleService {

    DoctorScheduleResponse createSchedule(DoctorScheduleRequest request);

    List<DoctorScheduleResponse> getSchedule(Long doctorId);

    void deleteSchedule(Long id);

    void createWeeklySchedule(DoctorWeeklyScheduleRequest request);

    List<DoctorWeeklyScheduleResponse> getWeeklySchedule(LocalDate startDate);

    void updateSchedule(Long scheduleId,UpdateDoctorScheduleRequest request);

}