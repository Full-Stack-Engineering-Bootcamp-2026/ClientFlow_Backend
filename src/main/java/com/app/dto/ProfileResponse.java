package com.app.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponse {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private String role;

    private String officialRole;

    private String specialization;

    private String profileImage;

    private LocalDateTime createdAt;
}