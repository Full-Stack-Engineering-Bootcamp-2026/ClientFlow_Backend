package com.app.service.patient;

import com.app.dto.patient.request.BookAppointmentRequest;
import com.app.dto.patient.response.AppointmentBookingResponse;
import com.app.dto.patient.response.DoctorDropdownResponse;
import com.app.dto.patient.response.PatientSearchResponse;

import java.util.List;

public interface AppointmentService {

    List<PatientSearchResponse> searchPatients(String keyword);

    AppointmentBookingResponse bookAppointment(BookAppointmentRequest request);
    List<DoctorDropdownResponse> getAllDoctors();
}