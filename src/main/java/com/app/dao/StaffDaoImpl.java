package com.app.dao;

import com.app.entity.Staff;
import com.app.exception.ResourceNotFoundException;
import com.app.exception.UnauthorizedException;
import com.app.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StaffDaoImpl implements StaffDao{

    private final StaffRepository staffRepository;

    public Staff getById(Long id) {

        return staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

    }

    public Staff getByEmail(String email) {

        return staffRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    public long getTotalStaffCount() {
        return staffRepository.count();
    }

    public long getActiveDoctorCount() {
        return staffRepository
                .countByRole_NameAndIsActiveTrue("DOCTOR");
    }

    public long countByRoleId(Long roleId) {
        return staffRepository.countByRoleId(roleId);
    }

    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    public boolean existsByEmail(String email) {
        return staffRepository.existsByEmail(email);
    }

    public List<Staff> getActiveDoctors() {

        return staffRepository
                .findByRole_NameAndIsActiveTrue("DOCTOR");
    }

    public Staff update(Staff staff) {
        return staffRepository.save(staff);
    }

    public Page<Staff> getAllStaff(Pageable pageable) {
        return staffRepository.findAll(pageable);
    }

}