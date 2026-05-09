package com.app.service;

import com.app.dto.AppointmentRequest;
import com.app.dto.AppointmentResponse;
import com.app.dto.patient.response.DoctorDropdownResponse;
import com.app.dto.patient.response.PatientSearchResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse bookAppointment(AppointmentRequest request);

    List<PatientSearchResponse> searchPatients(String keyword);

    List<DoctorDropdownResponse> getAllDoctors();
}