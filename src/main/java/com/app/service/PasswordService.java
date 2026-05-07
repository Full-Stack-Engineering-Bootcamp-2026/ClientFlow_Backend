package com.app.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.ForgotPasswordRequest;
import com.app.dto.ResetPasswordRequest;
import com.app.entity.PasswordResetToken;
import com.app.entity.Staff;
import com.app.exception.BadRequestException;
import com.app.repository.PasswordResetTokenRepository;
import com.app.repository.StaffRepository;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {

     private final StaffRepository staffRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    private static final long EXPIRY_MINUTES = 15;

        @Transactional
     public void forgotPassword(ForgotPasswordRequest request){
        String email = request.getEmail();

    
       staffRepository.findByEmail(email).ifPresent(user -> {

           
            tokenRepository.deleteByStaffId(user.getId());

            String token = UUID.randomUUID().toString();

            PasswordResetToken entity = PasswordResetToken.builder()
                    .staff(user)
                    .token(token)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES))
                    .build();

            tokenRepository.save(entity);

          
            emailService.sendForgotPasswordEmail(user.getEmail(), token);
        });

        
    }
    @Transactional
   public void resetPassword(String token, String newPassword) {

    PasswordResetToken tokenEntity = tokenRepository.findByToken(token)
            .orElseThrow(() ->
                    new BadRequestException("Invalid or expired token")
            );

    if (tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
        tokenRepository.delete(tokenEntity);
        throw new BadRequestException("Token expired");
    }

    Staff user = tokenEntity.getStaff();

    user.setPasswordHash(passwordEncoder.encode(newPassword));

    staffRepository.save(user);

    tokenRepository.delete(tokenEntity);
}
}