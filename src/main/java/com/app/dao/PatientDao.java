package com.app.dao;

import com.app.entity.Patient;
import com.app.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Optional;

public interface PatientDao {

    Optional<Patient> findByMobile(String mobile) ;

    public Patient save(Patient patient);
}