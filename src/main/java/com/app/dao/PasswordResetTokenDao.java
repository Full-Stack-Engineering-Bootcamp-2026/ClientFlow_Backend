package com.app.dao;

import com.app.entity.PasswordResetToken;
import com.app.exception.BadRequestException;
import com.app.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

public interface PasswordResetTokenDao {

    public void deleteByStaffId(Long staffId) ;

    public PasswordResetToken save(PasswordResetToken token) ;

    public PasswordResetToken getByToken(String token) ;

    public void delete(PasswordResetToken token) ;
}