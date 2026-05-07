package com.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);

     boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

     long countByRole_NameAndIsActiveTrue(String roleName);
     
     List<Staff> findByRole_NameAndIsActiveTrue(String roleName);

     long countByRoleId(Long roleId);
}