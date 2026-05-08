package com.app.dao;

import com.app.entity.DoctorSchedule;
import com.app.enums.DayOfWeek;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.DoctorScheduleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;


public interface DoctorScheduleDao {

    DoctorSchedule getDoctorSchedule(Long doctorId,DayOfWeek day) ;

    public DoctorSchedule getById(Long id) ;

    public boolean existsByDoctorAndDay(Long doctorId,DayOfWeek day) ;

    public List<DoctorSchedule> getActiveSchedulesByDoctor(Long doctorId) ;

    public long countActiveDoctorsByDay(DayOfWeek day);

    public DoctorSchedule save(DoctorSchedule schedule) ;

    public void delete(DoctorSchedule schedule) ;
}