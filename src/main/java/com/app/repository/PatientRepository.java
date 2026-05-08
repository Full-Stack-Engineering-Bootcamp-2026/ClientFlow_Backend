package com.app.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Patient;


public interface PatientRepository extends JpaRepository<Patient, Long>  {

     Optional<Patient> findByMobile(String mobile);




}