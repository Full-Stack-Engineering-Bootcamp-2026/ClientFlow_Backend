package com.app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfilePhotoService {

    String uploadProfilePhoto(
            MultipartFile file,
            String staffEmail
    );
    ResponseEntity<byte[]> getProfilePhoto(
        String staffEmail
);
}