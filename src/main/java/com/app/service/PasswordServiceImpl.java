package com.app.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.PasswordResetTokenDao;
import com.app.dao.StaffDao;
import com.app.dto.ForgotPasswordRequest;
import com.app.entity.PasswordResetToken;
import com.app.entity.Staff;
import com.app.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService{

    private final StaffDao staffDao;
    private final PasswordResetTokenDao tokenDao;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final long EXPIRY_MINUTES = 15;

    @Transactional
    public void forgotPassword(
            ForgotPasswordRequest request
    ) {

        String email = request.getEmail();

        try {

            Staff user = staffDao.getByEmail(email);

            tokenDao.deleteByStaffId(user.getId());

            String token = UUID.randomUUID().toString();

            PasswordResetToken entity =
                    PasswordResetToken.builder()
                            .staff(user)
                            .token(token)
                            .createdAt(LocalDateTime.now())
                            .expiresAt(
                                    LocalDateTime.now()
                                            .plusMinutes(
                                                    EXPIRY_MINUTES
                                            )
                            )
                            .build();

            tokenDao.save(entity);

            emailService.sendForgotPasswordEmail(
                    user.getEmail(),
                    token
            );

        } catch (Exception ignored) {

        }
    }

    @Transactional
    public void resetPassword(
            String token,
            String newPassword
    ) {

        PasswordResetToken tokenEntity =
                tokenDao.getByToken(token);

        if (tokenEntity.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            tokenDao.delete(tokenEntity);

            throw new BadRequestException(
                    "Token expired");
        }

        Staff user = tokenEntity.getStaff();

        user.setPasswordHash(
                passwordEncoder.encode(newPassword)
        );

        staffDao.update(user);

        tokenDao.delete(tokenEntity);
    }
}