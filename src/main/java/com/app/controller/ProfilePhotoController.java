package com.app.controller;

import com.app.response.ApiResponse;

import com.app.service.ProfilePhotoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfilePhotoController {

    private final ProfilePhotoService profilePhotoService;

    @PutMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadProfilePhoto(

            @RequestParam("file") MultipartFile file,

            Authentication authentication) {

        String photoUrl = profilePhotoService.uploadProfilePhoto(
                file,
                authentication.getName());

        return ResponseEntity.ok(

                ApiResponse.<String>builder()
                        .success(true)
                        .message(
                                "Profile photo uploaded successfully")
                        .data(photoUrl)
                        .build());
    }

    @GetMapping("/photo/view")
        public ResponseEntity<byte[]> viewProfilePhoto(
            Authentication authentication)   {

                    return profilePhotoService.getProfilePhoto(authentication.getName());
    }
}