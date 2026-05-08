package com.app.dao;

import com.app.entity.Role;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDaoImpl implements RoleDao {

    private final RoleRepository roleRepository;

    public Role getById(Long id) {

        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }
}