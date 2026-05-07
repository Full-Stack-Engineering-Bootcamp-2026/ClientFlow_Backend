package com.app.dao;

import com.app.entity.DoctorSchedule;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.DoctorScheduleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorScheduleDao {

    private final DoctorScheduleRepository scheduleRepository;

    public DoctorSchedule getScheduleById(Long id) {

        return scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Schedule not found"));
    }
}