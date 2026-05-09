package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StaffResponse {


    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String officialRole;
    private Boolean isActive;
    
}