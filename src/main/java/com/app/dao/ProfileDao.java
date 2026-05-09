package com.app.dao;

import com.app.entity.Staff;

import java.util.Optional;

public interface ProfileDao {

    Optional<Staff> findByEmail(String email);

    Staff save(Staff staff);
}