package com.app.service;

import org.springframework.data.domain.Page;

import com.app.dto.CreateStaffRequest;
import com.app.dto.CreateStaffResponse;
import com.app.dto.StaffResponse;



public interface StaffService {

    CreateStaffResponse createStaff(CreateStaffRequest request) ;

     Page<StaffResponse> getAllStaff(int page,int size) ;

    void updateStaffStatus(Long staffId,Boolean isActive) ;

}