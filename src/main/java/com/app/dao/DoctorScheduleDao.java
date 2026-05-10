package com.app.dao;

import com.app.entity.Appointment;
import com.app.entity.DoctorSchedule;
import com.app.enums.DayOfWeek;

import java.util.List;

public interface DoctorScheduleDao {

    DoctorSchedule getDoctorSchedule(Long doctorId, DayOfWeek day);
    DoctorSchedule getAnyDoctorSchedule(Long doctorId, DayOfWeek day);

    public DoctorSchedule getById(Long id);

    public boolean existsByDoctorAndDay(Long doctorId, DayOfWeek day);

    public List<DoctorSchedule> getActiveSchedulesByDoctor(Long doctorId);

    public long countActiveDoctorsByDay(DayOfWeek day);

    public DoctorSchedule save(DoctorSchedule schedule);

    public void delete(DoctorSchedule schedule);

    Appointment getAppointmentById(Long appointmentId);

    boolean existsInProgressAppointment(Long doctorId);
}