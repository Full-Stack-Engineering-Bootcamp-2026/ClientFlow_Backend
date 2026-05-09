package com.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadProfilePhotoUrlRequest {

    private String fileName;

    private String contentType;
}