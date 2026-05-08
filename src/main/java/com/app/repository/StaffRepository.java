package com.app.repository;

import com.app.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface StaffRepository extends
        JpaRepository<Staff, Long>,
        JpaSpecificationExecutor<Staff> {

    Optional<Staff> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    @Query("""
        SELECT s
        FROM Staff s
        WHERE s.role.name = 'DOCTOR'
        AND s.isActive = true
        ORDER BY s.fullName ASC
        """)
List<Staff> findAllActiveDoctors();
long countByRole_NameAndIsActiveTrue(String roleName);
     
     List<Staff> findByRole_NameAndIsActiveTrue(String roleName);

     long countByRoleId(Long roleId);
}