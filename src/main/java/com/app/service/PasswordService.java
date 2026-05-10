package com.app.service;

import com.app.dto.ForgotPasswordRequest;



public interface PasswordService {
     void forgotPassword(
            ForgotPasswordRequest request
    ) ;
 void resetPassword(
            String token,
            String newPassword
    ) ;
}