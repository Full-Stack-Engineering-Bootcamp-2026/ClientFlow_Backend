package com.app.service;

import com.app.dao.ProfileDao;
import com.app.dto.UpdateProfileRequest;
import com.app.dto.ProfileResponse;
import com.app.entity.Staff;
import com.app.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDao profileDao;

    @Override
    public ProfileResponse getMyProfile() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Staff staff = profileDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        return mapToResponse(staff);
    }

    @Override
    public ProfileResponse updateMyProfile(UpdateProfileRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Staff staff = profileDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        staff.setFullName(request.getName());
        staff.setPhone(request.getPhoneNumber());
        staff.setSpecialization(request.getSpecialization());

        Staff updatedStaff = profileDao.save(staff);

        return mapToResponse(updatedStaff);
    }

    private ProfileResponse mapToResponse(Staff staff) {

        return ProfileResponse.builder()
                .id(staff.getId())
                .name(staff.getFullName())
                .email(staff.getEmail())
                .phoneNumber(staff.getPhone())
                .role(staff.getRole().getName())
                .officialRole(staff.getOfficialRole())
                .specialization(staff.getSpecialization())
                .profileImage(
                 staff.getProfilePhotoUrl())
                .build();
    }
}