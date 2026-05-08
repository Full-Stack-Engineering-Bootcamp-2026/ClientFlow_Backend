package com.app.dao;

import com.app.entity.Patient;
import com.app.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PatientDaoImpl implements PatientDao {

    private final PatientRepository patientRepository;

    public Optional<Patient> findByMobile(String mobile) {
        return patientRepository.findByMobile(mobile);
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }
}