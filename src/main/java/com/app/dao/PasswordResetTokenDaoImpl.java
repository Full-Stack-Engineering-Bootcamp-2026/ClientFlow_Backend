package com.app.dao;

import com.app.entity.PasswordResetToken;
import com.app.exception.BadRequestException;
import com.app.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenDaoImpl implements PasswordResetTokenDao{

    private final PasswordResetTokenRepository tokenRepository;

    public void deleteByStaffId(Long staffId) {tokenRepository.deleteByStaffId(staffId);
    }

    public PasswordResetToken save(PasswordResetToken token) {

        return tokenRepository.save(token);
    }

    public PasswordResetToken getByToken(String token) {

        return tokenRepository.findByToken(token).orElseThrow(() ->new BadRequestException("Invalid or expired token"));
    }

    public void delete(PasswordResetToken token) {
        tokenRepository.delete(token);
    }
}