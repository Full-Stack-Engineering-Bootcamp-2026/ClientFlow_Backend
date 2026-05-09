package com.app.service;

import com.app.dao.StaffDao;

import com.app.dto.UpdateProfilePhotoRequest;
import com.app.dto.UploadProfilePhotoUrlRequest;
import com.app.dto.UploadProfilePhotoUrlResponse;

import com.app.entity.Staff;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilePhotoServiceImpl
        implements ProfilePhotoService {

    private final StaffDao staffDao;

    private final S3Presigner s3Presigner;

    @Value("${cloud.s3.bucket}")
    private String bucketName;

    @Value("${cloud.s3.endpoint}")
    private String endpoint;

    @Override
    public UploadProfilePhotoUrlResponse generateUploadUrl(
            UploadProfilePhotoUrlRequest request,
            String staffEmail) {

        Staff staff = staffDao.getByEmail(staffEmail);

        String contentType = request.getContentType();

        if (!contentType.equals("image/jpeg")
                &&
                !contentType.equals("image/png")
                &&
                !contentType.equals("image/webp")) {

            throw new RuntimeException(
                    "Invalid image type");
        }

        String fileExtension = request.getFileName()
                .substring(
                        request.getFileName()
                                .lastIndexOf("."));

        String fileKey = "profiles/"
                + staff.getId()
                + "/"
                + UUID.randomUUID()
                + fileExtension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(
                        Duration.ofMinutes(10))
                .putObjectRequest(
                        putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner
                .presignPutObject(
                        presignRequest);

        String uploadUrl = presignedRequest.url().toString();

        String fileUrl = endpoint
                + "/"
                + bucketName
                + "/"
                + fileKey;

        return new UploadProfilePhotoUrlResponse(
                uploadUrl,
                fileUrl,
                fileKey);
    }
    
    @Override
    public void updateProfilePhoto(
            UpdateProfilePhotoRequest request,
            String staffEmail) {

        Staff staff = staffDao.findByEmail(staffEmail);

        staff.setProfilePhotoUrl(
                request.getProfilePhotoUrl());

        staff.setProfilePhotoKey(
                request.getProfilePhotoKey());

        staffDao.save(staff);
    }
}