package com.app.controller;

import com.app.dto.UpdateProfileRequest;
import com.app.dto.ProfileResponse;
import com.app.response.ApiResponse;
import com.app.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile() {

        ProfileResponse response = profileService.getMyProfile();

        return ResponseEntity.ok(
                ApiResponse.<ProfileResponse>builder()
                        .success(true)
                        .message("Profile fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {

        ProfileResponse response = profileService.updateMyProfile(request);

        return ResponseEntity.ok(
                ApiResponse.<ProfileResponse>builder()
                        .success(true)
                        .message("Profile updated successfully")
                        .data(response)
                        .build()
        );
    }
}