package com.app.dao;

import com.app.entity.Role;

import java.util.List;

public interface RoleDao {

 Role getById(Long id);

 Role getByName(String name);

 List<Role> getAllRoles();
}