package com.app.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.PasswordResetTokenDao;
import com.app.dao.RoleDao;
import com.app.dao.StaffDao;
import com.app.dto.CreateStaffRequest;
import com.app.dto.CreateStaffResponse;
import com.app.dto.RoleResponse;
import com.app.dto.StaffResponse;
import com.app.entity.PasswordResetToken;
import com.app.entity.Role;
import com.app.entity.Staff;
import com.app.exception.DuplicateResourceException;

import lombok.RequiredArgsConstructor;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

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
 int size,
 String search,
 String role,
 Boolean isActive
 ) {

 Pageable pageable = PageRequest.of(
 page,
 size,
 Sort.by("createdAt").descending()
 );

 Page<Staff> staffPage =
 staffDao.getAllStaff(
 buildStaffSpecification(
 search,
 role,
 isActive
 ),
 pageable
 );

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

 public List<RoleResponse> getAllRoles() {

 return roleDao.getAllRoles()
 .stream()
 .map(role -> new RoleResponse(
 role.getId(),
 role.getName()
 ))
 .toList();
 }

 private Specification<Staff> buildStaffSpecification(
 String search,
 String role,
 Boolean isActive
 ) {

 return (root, query, cb) -> {
 List<Predicate> predicates =
 new ArrayList<>();

 if (search != null && !search.isBlank()) {
 predicates.add(
 cb.like(
 cb.lower(root.get("fullName")),
 "%" + search.toLowerCase() + "%"
 )
 );
 }

 if (role != null && !role.isBlank()) {
 predicates.add(
 cb.equal(
 cb.upper(root.get("role").get("name")),
 role.toUpperCase()
 )
 );
 }

 if (isActive != null) {
 predicates.add(
 cb.equal(root.get("isActive"), isActive)
 );
 }

 return cb.and(
 predicates.toArray(new Predicate[0])
 );
 };
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
