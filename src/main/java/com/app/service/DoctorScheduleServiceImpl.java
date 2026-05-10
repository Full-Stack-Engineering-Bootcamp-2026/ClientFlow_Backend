package com.app.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.AdminDoctorSchedulePageResponse;
import com.app.dto.AdminDoctorScheduleRowResponse;
import com.app.dto.AdminDoctorScheduleStatsResponse;
import com.app.dto.ChangeDoctorScheduleRequest;
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

    private static final LocalTime STANDARD_START_TIME = LocalTime.of(9, 0);
    private static final LocalTime STANDARD_END_TIME = LocalTime.of(17, 0);
    private static final int STANDARD_MAX_APPOINTMENTS = 20;

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

    public AdminDoctorSchedulePageResponse getAdminSchedulePage(
            LocalDate startDate,
            String specialization,
            String status,
            int page,
            int size
    ) {

        LocalDate startOfWeek =
                startDate.with(java.time.DayOfWeek.MONDAY);

        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<Staff> doctors = staffDao.getActiveDoctors();

        List<AdminDoctorScheduleRowResponse> rows =
                new ArrayList<>();

        long queueCapacityToday = 0;
        long onLeaveThisWeek = 0;
        Set<String> specializations = new HashSet<>();

        DayOfWeek today = DayOfWeek.valueOf(
                LocalDate.now()
                        .getDayOfWeek()
                        .name()
                        .substring(0, 3)
        );

        for (Staff doctor : doctors) {
            String doctorSpecialization =
                    doctor.getSpecialization() == null
                            || doctor.getSpecialization().isBlank()
                            ? "General Medicine"
                            : doctor.getSpecialization();

            specializations.add(doctorSpecialization);

            List<DoctorSchedule> schedules =
                    scheduleDao.getActiveSchedulesByDoctor(
                            doctor.getId()
                    );

            List<LeaveException> leaves =
                    leaveDao.getDoctorLeavesBetween(
                            doctor.getId(),
                            startOfWeek,
                            endOfWeek
                    );

            List<String> workingDays = schedules.stream()
                    .map(s -> s.getDayOfWeek().name())
                    .toList();

            List<String> leaveDates = leaves.stream()
                    .map(l -> l.getExceptionDate().toString())
                    .toList();

            boolean doctorOnLeaveThisWeek =
                    !leaveDates.isEmpty();
            boolean doctorOnLeaveToday =
                    leaveDates.contains(LocalDate.now().toString());

            if (doctorOnLeaveThisWeek) {
                onLeaveThisWeek++;
            }

            for (DoctorSchedule schedule : schedules) {
                if (schedule.getDayOfWeek() == today
                        && !doctorOnLeaveToday) {
                    queueCapacityToday +=
                            schedule.getMaxAppointments();
                    break;
                }
            }

            DoctorSchedule firstSchedule =
                    schedules.isEmpty() ? null : schedules.get(0);

            rows.add(
                    new AdminDoctorScheduleRowResponse(
                            doctor.getId(),
                            doctor.getFullName(),
                            doctorSpecialization,
                            firstSchedule == null
                                    ? STANDARD_START_TIME.toString()
                                    : firstSchedule.getStartTime().toString(),
                            firstSchedule == null
                                    ? STANDARD_END_TIME.toString()
                                    : firstSchedule.getEndTime().toString(),
                            firstSchedule == null
                                    ? STANDARD_MAX_APPOINTMENTS
                                    : firstSchedule.getMaxAppointments(),
                            workingDays,
                            leaveDates,
                            doctorOnLeaveThisWeek
                    )
            );
        }

        List<AdminDoctorScheduleRowResponse> filteredRows =
                rows.stream()
                        .filter(row ->
                                specialization == null
                                        || specialization.isBlank()
                                        || row.getSpecialization()
                                                .equalsIgnoreCase(
                                                        specialization
                                                )
                        )
                        .filter(row -> {
                            if (status == null
                                    || status.equalsIgnoreCase("ALL")) {
                                return true;
                            }

                            if (status.equalsIgnoreCase("ON_LEAVE")) {
                                return row.getOnLeaveThisWeek();
                            }

                            if (status.equalsIgnoreCase("WORKING")) {
                                return !row.getOnLeaveThisWeek();
                            }

                            return true;
                        })
                        .toList();

        int safePage = Math.max(0, page);
        int safeSize = Math.max(1, size);
        int fromIndex = Math.min(
                safePage * safeSize,
                filteredRows.size()
        );
        int toIndex = Math.min(
                fromIndex + safeSize,
                filteredRows.size()
        );

        List<AdminDoctorScheduleRowResponse> pageRows =
                filteredRows.subList(fromIndex, toIndex);

        int totalPages =
                (int) Math.ceil(
                        filteredRows.size() / (double) safeSize
                );

        return new AdminDoctorSchedulePageResponse(
                pageRows,
                filteredRows.size(),
                totalPages,
                safeSize,
                safePage,
                new AdminDoctorScheduleStatsResponse(
                        doctors.size(),
                        queueCapacityToday,
                        onLeaveThisWeek
                ),
                specializations.stream()
                        .sorted()
                        .toList()
        );
    }

    @Transactional
    public void changeDoctorSchedule(
            ChangeDoctorScheduleRequest request
    ) {

        Staff doctor = staffDao.getById(request.getDoctorId());

        if (!doctor.getRole().getName().equals("DOCTOR")) {
            throw new BadRequestException("Invalid doctor");
        }

        LocalTime startTime =
                request.getStartTime() == null
                        ? STANDARD_START_TIME
                        : request.getStartTime();

        LocalTime endTime =
                request.getEndTime() == null
                        ? STANDARD_END_TIME
                        : request.getEndTime();

        Integer maxAppointments =
                request.getMaxAppointments() == null
                        ? STANDARD_MAX_APPOINTMENTS
                        : request.getMaxAppointments();

        if (startTime.isAfter(endTime)) {
            throw new BadRequestException(
                    "Start time must be before end time"
            );
        }

        for (LocalDate date : request.getDates()) {
            DayOfWeek day = DayOfWeek.valueOf(
                    date.getDayOfWeek()
                            .name()
                            .substring(0, 3)
            );

            DoctorSchedule schedule =
                    scheduleDao.getAnyDoctorSchedule(
                            doctor.getId(),
                            day
                    );

            if (schedule == null) {
                schedule = DoctorSchedule.builder()
                        .doctor(doctor)
                        .dayOfWeek(day)
                        .startTime(startTime)
                        .endTime(endTime)
                        .maxAppointments(maxAppointments)
                        .isActive(true)
                        .build();
            } else {
                schedule.setStartTime(startTime);
                schedule.setEndTime(endTime);
                schedule.setMaxAppointments(maxAppointments);
                schedule.setIsActive(true);
            }

            scheduleDao.save(schedule);
        }
    }
}