package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadProfilePhotoUrlResponse {

    private String uploadUrl;

    private String fileUrl;

    private String fileKey;
}