package com.app.service.patient;

import com.app.dto.patient.request.RegisterPatientRequest;
import com.app.dto.patient.response.PatientResponse;

public interface PatientService {

    PatientResponse registerPatient(RegisterPatientRequest request);
}