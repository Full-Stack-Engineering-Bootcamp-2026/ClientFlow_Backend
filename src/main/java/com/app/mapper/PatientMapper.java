package com.app.mapper;

import com.app.dto.patient.request.RegisterPatientRequest;
import com.app.dto.patient.response.PatientResponse;
import com.app.entity.Patient;
import com.app.entity.Staff;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toEntity(RegisterPatientRequest request, Staff registeredBy) {

        return Patient.builder()
                .fullName(request.getFullName().trim())
                .mobile(request.getMobile().trim())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .bloodGroup(request.getBloodGroup())
                .address(request.getAddress())
                .medicalNotes(request.getMedicalNotes())
                .registeredBy(registeredBy)
                .build();
    }

    public PatientResponse toResponse(Patient patient) {

        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .mobile(patient.getMobile())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .bloodGroup(patient.getBloodGroup())
                .address(patient.getAddress())
                .medicalNotes(patient.getMedicalNotes())
                .registeredBy(patient.getRegisteredBy().getFullName())
                .registeredAt(patient.getCreatedAt())
                .build();
    }
}