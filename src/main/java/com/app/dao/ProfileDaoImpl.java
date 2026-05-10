package com.app.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.app.entity.Staff;
import com.app.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProfileDaoImpl implements ProfileDao{

    private final StaffRepository staffRepository;

    @Override
    public Optional<Staff> findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    
}