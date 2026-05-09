package com.app.repository;
import java.util.List;
import com.app.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PatientRepository extends
        JpaRepository<Patient, Long>,
        JpaSpecificationExecutor<Patient> {

    Optional<Patient> findByMobile(String mobile);

    boolean existsByMobile(String mobile);
    List<Patient> findByFullNameContainingIgnoreCase(String fullName);
}