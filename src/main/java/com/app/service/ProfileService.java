package com.app.service;

import com.app.dto.ProfileResponse;
import com.app.dto.UpdateProfileRequest;


public interface ProfileService {

    ProfileResponse getMyProfile();

    ProfileResponse updateMyProfile(UpdateProfileRequest request);

    
}