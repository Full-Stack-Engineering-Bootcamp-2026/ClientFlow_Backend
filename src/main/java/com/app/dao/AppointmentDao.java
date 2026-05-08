package com.app.dao;

import java.time.LocalDate;

import com.app.entity.Appointment;
import com.app.enums.AppointmentStatus;
import com.app.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;


public interface AppointmentDao {


    public long getTotalAppointmentsCount() ;

    public long getCompletedAppointmentsCount() ;

    public long getCancelledAppointmentsCount() ;

    public boolean existsPatientBooking(Long doctorId,Long patientId,LocalDate date) ;

    public long countDoctorAppointments(Long doctorId,LocalDate date) ;

    public int getNextQueueNumber(Long doctorId,LocalDate date) ;

    public Appointment save(Appointment appointment) ;
    
    public long countAppointmentsByDate(LocalDate date) ;
}