package com.app.dao;

import com.app.entity.PasswordResetToken;


public interface PasswordResetTokenDao {

    public void deleteByStaffId(Long staffId) ;

    public PasswordResetToken save(PasswordResetToken token) ;

    public PasswordResetToken getByToken(String token) ;

    public void delete(PasswordResetToken token) ;
}