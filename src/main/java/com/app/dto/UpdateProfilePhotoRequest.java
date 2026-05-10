package com.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfilePhotoRequest {

    private String profilePhotoUrl;

    private String profilePhotoKey;
}