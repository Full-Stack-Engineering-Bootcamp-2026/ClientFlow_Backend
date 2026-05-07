package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.DoctorScheduleRequest;
import com.app.dto.DoctorScheduleResponse;
import com.app.entity.DoctorSchedule;
import com.app.entity.Staff;
import com.app.exception.BadRequestException;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.DoctorScheduleRepository;
import com.app.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final StaffRepository staffRepository;

    @Transactional
    public DoctorScheduleResponse createSchedule(DoctorScheduleRequest request) {

        Staff doctor = staffRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (!doctor.getRole().getName().equals("DOCTOR")) {
            throw new BadRequestException("Staff is not a doctor");
        }
        if (scheduleRepository.existsByDoctorIdAndDayOfWeek(
                request.getDoctorId(),
                request.getDayOfWeek())) {
            throw new BadRequestException("Schedule already exists for this day");
        }
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }

        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxAppointments(request.getMaxAppointments())
                .isActive(true)
                .build();

        scheduleRepository.save(schedule);

        return mapToResponse(schedule);

    }

    public List<DoctorScheduleResponse> getSchedule(Long doctorId) {

        List<DoctorSchedule> schedules = scheduleRepository.findByDoctorIdAndIsActiveTrue(doctorId);

        return schedules.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public DoctorScheduleResponse updateSchedule(Long id, DoctorScheduleRequest request) {

        DoctorSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setMaxAppointments(request.getMaxAppointments());
        schedule.setIsActive(request.getIsActive());

        return mapToResponse(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {

        DoctorSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        scheduleRepository.delete(schedule);
    }

    private DoctorScheduleResponse mapToResponse(DoctorSchedule s) {
        return new DoctorScheduleResponse(
                s.getId(),
                s.getDoctor().getId(),
                s.getDoctor().getFullName(),
                s.getDayOfWeek(),
                s.getStartTime(),
                s.getEndTime(),
                s.getMaxAppointments(),
                s.getIsActive());
    }

}