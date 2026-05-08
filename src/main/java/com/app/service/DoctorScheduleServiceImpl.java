package com.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.DoctorScheduleDao;
import com.app.dao.LeaveDao;
import com.app.dao.StaffDao;
import com.app.dto.DoctorScheduleRequest;
import com.app.dto.DoctorScheduleResponse;
import com.app.dto.DoctorWeeklyScheduleRequest;
import com.app.dto.DoctorWeeklyScheduleResponse;
import com.app.dto.UpdateDoctorScheduleRequest;
import com.app.entity.DoctorSchedule;
import com.app.entity.LeaveException;
import com.app.entity.Staff;
import com.app.enums.DayOfWeek;
import com.app.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService{

    private final DoctorScheduleDao scheduleDao;
    private final StaffDao staffDao;
    private final LeaveDao leaveDao;

    @Transactional
    public DoctorScheduleResponse createSchedule(
            DoctorScheduleRequest request
    ) {

        Staff doctor = staffDao.getById(request.getDoctorId());

        if (!doctor.getRole().getName().equals("DOCTOR")) {
            throw new BadRequestException("Staff is not a doctor");
        }

        boolean exists =
                scheduleDao.existsByDoctorAndDay(
                        request.getDoctorId(),
                        request.getDayOfWeek()
                );

        if (exists) {
            throw new BadRequestException(
                    "Schedule already exists for this day");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BadRequestException(
                    "Start time must be before end time");
        }

        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxAppointments(request.getMaxAppointments())
                .isActive(true)
                .build();

        scheduleDao.save(schedule);

        return mapToResponse(schedule);
    }

    public List<DoctorScheduleResponse> getSchedule(Long doctorId) {

        List<DoctorSchedule> schedules =
                scheduleDao.getActiveSchedulesByDoctor(doctorId);

        return schedules.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void deleteSchedule(Long id) {

        DoctorSchedule schedule = scheduleDao.getById(id);

        scheduleDao.delete(schedule);
    }

    private DoctorScheduleResponse mapToResponse(
            DoctorSchedule s
    ) {

        return new DoctorScheduleResponse(
                s.getId(),
                s.getDoctor().getId(),
                s.getDoctor().getFullName(),
                s.getDayOfWeek(),
                s.getStartTime(),
                s.getEndTime(),
                s.getMaxAppointments(),
                s.getIsActive()
        );
    }

    @Transactional
    public void createWeeklySchedule(
            DoctorWeeklyScheduleRequest request
    ) {

        Staff doctor = staffDao.getById(request.getDoctorId());

        if (!doctor.getRole().getName().equals("DOCTOR")) {
            throw new BadRequestException("Invalid doctor");
        }

        for (DayOfWeek day : request.getDays()) {

            boolean exists =
                    scheduleDao.existsByDoctorAndDay(
                            doctor.getId(),
                            day
                    );

            if (exists) {
                continue;
            }

            DoctorSchedule schedule = DoctorSchedule.builder()
                    .doctor(doctor)
                    .dayOfWeek(day)
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .maxAppointments(request.getMaxAppointments())
                    .isActive(true)
                    .build();

            scheduleDao.save(schedule);
        }
    }

    public List<DoctorWeeklyScheduleResponse>
    getWeeklySchedule(LocalDate startDate) {

        LocalDate startOfWeek =
                startDate.with(java.time.DayOfWeek.MONDAY);

        LocalDate endOfWeek =
                startOfWeek.plusDays(6);

        List<Staff> doctors =
                staffDao.getActiveDoctors();

        List<DoctorWeeklyScheduleResponse> response =
                new ArrayList<>();

        for (Staff doctor : doctors) {

            List<DoctorSchedule> schedules =
                    scheduleDao.getActiveSchedulesByDoctor(
                            doctor.getId()
                    );

            if (schedules.isEmpty()) {
                continue;
            }

            List<String> workingDays = schedules.stream()
                    .map(s -> s.getDayOfWeek().name())
                    .toList();

            List<LeaveException> leaves =
                    leaveDao.getDoctorLeavesBetween(
                            doctor.getId(),
                            startOfWeek,
                            endOfWeek
                    );

            List<String> leaveDates = leaves.stream()
                    .map(l -> l.getExceptionDate().toString())
                    .toList();

            response.add(
                    new DoctorWeeklyScheduleResponse(
                            doctor.getId(),
                            doctor.getFullName(),
                            workingDays,
                            leaveDates
                    )
            );
        }

        return response;
    }

    @Transactional
    public void updateSchedule(
            Long scheduleId,
            UpdateDoctorScheduleRequest request
    ) {

        DoctorSchedule schedule =
                scheduleDao.getById(scheduleId);

        if (request.getStartTime() != null) {
            schedule.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            schedule.setEndTime(request.getEndTime());
        }

        if (request.getMaxAppointments() != null) {
            schedule.setMaxAppointments(
                    request.getMaxAppointments()
            );
        }

        if (request.getIsActive() != null) {
            schedule.setIsActive(request.getIsActive());
        }

        if (schedule.getStartTime()
                .isAfter(schedule.getEndTime())) {

            throw new BadRequestException(
                    "Start time must be before end time");
        }

        scheduleDao.save(schedule);
    }
}