package com.app.dao;

import com.app.entity.DoctorSchedule;
import com.app.enums.DayOfWeek;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.DoctorScheduleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DoctorScheduleDaoImpl implements DoctorScheduleDao{

    private final DoctorScheduleRepository scheduleRepository;

    public DoctorSchedule getDoctorSchedule(
            Long doctorId,
            DayOfWeek day
    ) {

        return scheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, day)
                .orElseThrow(() ->new ResourceNotFoundException("Doctor schedule not found"));
    }

    public DoctorSchedule getById(Long id) {

        return scheduleRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Schedule not found"));
    }

    public boolean existsByDoctorAndDay(
            Long doctorId,
            DayOfWeek day
    ) {

        return scheduleRepository
                .existsByDoctorIdAndDayOfWeek(doctorId,day);
    }

    public List<DoctorSchedule> getActiveSchedulesByDoctor(
            Long doctorId
    ) {

        return scheduleRepository
                .findByDoctorIdAndIsActiveTrue(doctorId);
    }

    public long countActiveDoctorsByDay(DayOfWeek day) {

        return scheduleRepository
                .countByDayOfWeekAndIsActiveTrue(day);
    }

    public DoctorSchedule save(DoctorSchedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void delete(DoctorSchedule schedule) {
        scheduleRepository.delete(schedule);
    }
}