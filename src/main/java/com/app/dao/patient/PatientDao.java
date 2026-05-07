package com.app.dao.patient;

import com.app.entity.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientDao {

    Patient save(Patient patient);

    boolean existsByMobile(String mobile);

    Optional<Patient> findByMobile(String mobile);

    List<Patient> searchPatients(String keyword);
}