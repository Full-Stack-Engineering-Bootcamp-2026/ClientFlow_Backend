package com.app.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.PasswordResetTokenDao;
import com.app.dao.RoleDao;
import com.app.dao.StaffDao;
import com.app.dto.CreateStaffRequest;
import com.app.dto.CreateStaffResponse;
import com.app.dto.StaffResponse;
import com.app.entity.PasswordResetToken;
import com.app.entity.Role;
import com.app.entity.Staff;
import com.app.exception.DuplicateResourceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService{

    private final PasswordEncoder passwordEncoder;

    private final StaffDao staffDao;

    private final RoleDao roleDao;

    private final PasswordResetTokenDao tokenDao;

    private final EmailService emailService;

    @Transactional
    public CreateStaffResponse createStaff(
            CreateStaffRequest request
    ) {

        if (staffDao.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists");
        }

        Role role = roleDao.getById(request.getRoleId());

        Staff staff = Staff.builder()
                .employeeId(generateEmployeeId(role))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(role)
                .phone(request.getPhone())
                .officialRole(request.getOfficialRole())
                .specialization(request.getSpecialization())
                .isActive(true)
                .build();

        staffDao.save(staff);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                PasswordResetToken.builder()
                        .staff(staff)
                        .token(token)
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusHours(24)
                        )
                        .createdAt(LocalDateTime.now())
                        .build();

        tokenDao.save(resetToken);

        emailService.sendStaffWelcomeEmail(
                staff.getEmail(),
                token
        );

        return new CreateStaffResponse(
                staff.getId(),
                staff.getFullName(),
                staff.getEmail(),
                role.getName()
        );
    }

    public Page<StaffResponse> getAllStaff(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Staff> staffPage =
                staffDao.getAllStaff(pageable);

        return staffPage.map(staff ->
                new StaffResponse(
                        staff.getId(),
                        staff.getFullName(),
                        staff.getEmail(),
                        staff.getRole().getName(),
                        staff.getIsActive()
                )
        );
    }

    @Transactional
    public void updateStaffStatus(
            Long staffId,
            Boolean isActive
    ) {

        Staff staff = staffDao.getById(staffId);

        staff.setIsActive(isActive);

        staffDao.update(staff);
    }

    private String generateEmployeeId(Role role) {

        String prefix;

        switch (role.getName().toUpperCase()) {

            case "DOCTOR":
                prefix = "DOC";
                break;

            case "NURSE":
                prefix = "NUR";
                break;

            case "ADMIN":
                prefix = "ADM";
                break;

            default:
                prefix = "STF";
        }

        long count =
                staffDao.countByRoleId(role.getId());

        return prefix +
                String.format("%03d", count + 1);
    }
}