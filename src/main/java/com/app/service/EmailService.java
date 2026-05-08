package com.app.service;


public interface EmailService {

    void sendForgotPasswordEmail(String toEmail, String token);

    void sendStaffWelcomeEmail(String toEmail, String token);
}