package com.app.dao;

import com.app.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientDao {

    Optional<Patient> findByMobile(String mobile);

    Patient save(Patient patient);

    boolean existsByMobile(String mobile);

    List<Patient> searchPatients(String keyword);

    Optional<Patient> findById(Long patientId);
}