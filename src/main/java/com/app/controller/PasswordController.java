package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ForgotPasswordRequest;
import com.app.dto.ResetPasswordRequest;
import com.app.response.ApiResponse;
import com.app.service.PasswordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordController {

     private final PasswordService passwordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        passwordService.forgotPassword(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("If the email exists, a reset link has been sent")
                        .build()
        );
    }

    @PostMapping("/reset-password")
public ResponseEntity<ApiResponse<Void>> resetPassword(
        @RequestParam String token,
        @Valid @RequestBody ResetPasswordRequest request) {

    passwordService.resetPassword(token, request.getNewPassword());

    return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                    .success(true)
                    .message("Password updated successfully")
                    .build()
    );
}
}