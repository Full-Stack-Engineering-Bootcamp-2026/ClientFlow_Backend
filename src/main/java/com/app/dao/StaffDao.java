package com.app.dao;

import com.app.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface StaffDao {

 Staff getById(Long id);

 Staff getByEmail(String email);

    Staff findByEmail(String email);

    long getTotalStaffCount();

 long getActiveDoctorCount();

 long countByRoleId(Long roleId);

 Staff save(Staff staff);

 boolean existsByEmail(String email);

 List<Staff> getActiveDoctors();

 Staff update(Staff staff);

 Page<Staff> getAllStaff(Pageable pageable);

 Page<Staff> getAllStaff(Specification<Staff> specification, Pageable pageable);

 List<Staff> getAllActiveDoctors();

 String getLastEmployeeId(String prefix) ;
}