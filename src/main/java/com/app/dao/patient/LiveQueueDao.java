package com.app.dao.patient;

import com.app.entity.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface LiveQueueDao {

    List<Appointment> getTodayAppointments(LocalDate appointmentDate);
}