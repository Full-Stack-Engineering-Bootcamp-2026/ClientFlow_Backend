package com.app.service.patient.impl;

import com.app.dao.patient.PatientDao;
import com.app.dto.patient.request.RegisterPatientRequest;
import com.app.dto.patient.response.PatientResponse;
import com.app.entity.Patient;
import com.app.entity.Staff;
import com.app.exception.DuplicateResourceException;
import com.app.exception.ResourceNotFoundException;
import com.app.mapper.PatientMapper;
import com.app.repository.StaffRepository;
import com.app.service.patient.PatientService;
import com.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;

    private final StaffRepository staffRepository;

    private final PatientMapper patientMapper;

    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public PatientResponse registerPatient(RegisterPatientRequest request) {

        if (patientDao.existsByMobile(request.getMobile())) {
            throw new DuplicateResourceException(
                    "Patient already exists with this mobile number"
            );
        }

        String email = securityUtil.getCurrentUserEmail();

        Staff loggedInStaff = staffRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logged in staff not found")
                );

        Patient patient = patientMapper.toEntity(request, loggedInStaff);

        Patient savedPatient = patientDao.save(patient);

        return patientMapper.toResponse(savedPatient);
    }
}