package com.app.service;

import com.app.dto.UpdateProfilePhotoRequest;
import com.app.dto.UploadProfilePhotoUrlRequest;
import com.app.dto.UploadProfilePhotoUrlResponse;

public interface ProfilePhotoService {

    UploadProfilePhotoUrlResponse generateUploadUrl(
            UploadProfilePhotoUrlRequest request,
            String staffEmail);

    void updateProfilePhoto(
            UpdateProfilePhotoRequest request,
            String staffEmail);
}