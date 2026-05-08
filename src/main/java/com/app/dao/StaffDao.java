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

public interface StaffDao {

    Staff getById(Long id) ;

    public Staff getByEmail(String email) ;

    public long getTotalStaffCount() ;

    public long getActiveDoctorCount() ;

    public long countByRoleId(Long roleId) ;

    public Staff save(Staff staff) ;

    public boolean existsByEmail(String email) ;

    public List<Staff> getActiveDoctors() ;

    public Staff update(Staff staff) ;

    public Page<Staff> getAllStaff(Pageable pageable);

}