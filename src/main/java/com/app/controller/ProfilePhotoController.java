package com.app.controller;

import com.app.response.ApiResponse;

import com.app.dto.UpdateProfilePhotoRequest;
import com.app.dto.UploadProfilePhotoUrlRequest;
import com.app.dto.UploadProfilePhotoUrlResponse;

import com.app.service.ProfilePhotoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile/photo")
@RequiredArgsConstructor
public class ProfilePhotoController {

    private final ProfilePhotoService profilePhotoService;

    @PostMapping("/upload-url")
    public ResponseEntity<ApiResponse> generateUploadUrl(
            @RequestBody UploadProfilePhotoUrlRequest request,
            Authentication authentication) {

        UploadProfilePhotoUrlResponse response = profilePhotoService
                .generateUploadUrl(
                        request,
                        authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Upload URL generated successfully",
                        response,
                        null));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse> updateProfilePhoto(
            @RequestBody UpdateProfilePhotoRequest request,
            Authentication authentication) {

        profilePhotoService
                .updateProfilePhoto(
                        request,
                        authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Profile photo updated successfully",
                        null,
                        null));
    }
}