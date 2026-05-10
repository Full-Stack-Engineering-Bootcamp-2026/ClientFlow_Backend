package com.app.dao;

import com.app.entity.Patient;
import com.app.exception.BadRequestException;
import com.app.repository.PatientRepository;
import com.app.specification.PatientSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PatientDaoImpl implements PatientDao {

    private final PatientRepository patientRepository;

    @Override
    public Optional<Patient> findByMobile(String mobile) {

        try {

            return patientRepository.findByMobile(mobile);

        } catch (DataAccessException ex) {

            log.error(
                    "Database error while fetching patient by mobile: {}",
                    ex.getMessage());

            throw new BadRequestException(
                    "Unable to fetch patient");
        }
    }

    @Override
    @Transactional
    public Patient save(Patient patient) {

        try {

            return patientRepository.save(patient);

        } catch (DataAccessException ex) {

            log.error(
                    "Database error while saving patient: {}",
                    ex.getMessage());

            throw new BadRequestException(
                    "Unable to save patient");
        }
    }

    @Override
    public boolean existsByMobile(String mobile) {

        try {

            return patientRepository.existsByMobile(mobile);

        } catch (DataAccessException ex) {

            log.error(
                    "Database error while checking patient mobile: {}",
                    ex.getMessage());

            throw new BadRequestException(
                    "Unable to validate patient mobile");
        }
    }

    @Override
    public List<Patient> searchPatients(String keyword) {

        try {

            Specification<Patient> specification = PatientSpecification.searchByKeyword(keyword);

            return patientRepository.findAll(specification);

        } catch (DataAccessException ex) {

            log.error(
                    "Database error while searching patients: {}",
                    ex.getMessage());

            throw new BadRequestException(
                    "Unable to search patients");
        }
    }

    @Override
    public Optional<Patient> findById(Long patientId) {

        try {

            return patientRepository.findById(patientId);

        } catch (DataAccessException ex) {

            log.error(
                    "Database error while fetching patient by id: {}",
                    ex.getMessage());

            throw new BadRequestException(
                    "Unable to fetch patient");
        }
    }
}