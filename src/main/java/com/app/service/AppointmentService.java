package com.app.service;

import com.app.dto.AppointmentRequest;
import com.app.dto.AppointmentResponse;



public interface AppointmentService {

    AppointmentResponse bookAppointment(AppointmentRequest request) ;
}