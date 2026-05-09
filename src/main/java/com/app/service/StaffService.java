package com.app.service;

import org.springframework.data.domain.Page;

import com.app.dto.CreateStaffRequest;
import com.app.dto.CreateStaffResponse;
import com.app.dto.RoleResponse;
import com.app.dto.StaffResponse;

import java.util.List;


public interface StaffService {

 CreateStaffResponse createStaff(CreateStaffRequest request) ;

 Page<StaffResponse> getAllStaff(
 int page,
 int size,
 String search,
 String role,
 Boolean isActive
 ) ;

 void updateStaffStatus(Long staffId,Boolean isActive) ;

 List<RoleResponse> getAllRoles();

}