package com.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dto.CreateStaffRequest;
import com.app.dto.CreateStaffResponse;
import com.app.entity.Role;
import com.app.entity.Staff;
import com.app.exception.BadRequestException;
import com.app.exception.DuplicateResourceException;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.RoleRepository;
import com.app.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final PasswordEncoder passwordEncoder;

    private final StaffRepository staffRepository;

    private final RoleRepository roleRepository;

     public CreateStaffResponse createStaff(CreateStaffRequest request){
        String email = request.getEmail();
        String fullName = request.getFullName();

        if (staffRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists");
        }

        if (staffRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new DuplicateResourceException("Employee ID already exists");
        }

         Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found")
                );

        if ("DOCTOR".equals(role.getName()) && request.getSpecialization() == null) {
            throw new BadRequestException("Specialization is required for doctor");
        }

        Staff staff = Staff.builder()
                .fullName(fullName)
                .email(email)
                .employeeId(request.getEmployeeId())
                .phone(request.getPhone())
                .officialRole(request.getOfficialRole())
                .specialization(request.getSpecialization())
                .role(role)
                .passwordHash(passwordEncoder.encode(request.getPassword())) 
                .isActive(true)
                .build();

        staffRepository.save(staff);
        return new CreateStaffResponse(
                staff.getId(),
                staff.getFullName(),
                staff.getEmail(),
                role.getName()
        );
     }
}