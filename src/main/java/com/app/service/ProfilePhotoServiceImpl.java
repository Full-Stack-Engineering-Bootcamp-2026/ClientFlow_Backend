package com.app.service;

import com.app.dao.StaffDao;

import com.app.entity.Staff;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import software.amazon.awssdk.core.ResponseBytes;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilePhotoServiceImpl
     implements ProfilePhotoService{

    private final StaffDao staffDao;

    private final S3Client s3Client;

    @Value("${cloud.s3.bucket}")
    private String bucketName;

    @Value("${cloud.s3.endpoint}")
    private String endpoint;

    @Override
    public String uploadProfilePhoto(
            MultipartFile file,
            String staffEmail) {

        try {

            Staff staff = staffDao.getByEmail(staffEmail);

            // VALIDATE FILE
            String contentType = file.getContentType();

            if (contentType == null
                    ||
                    (!contentType.equals("image/jpeg")
                            &&
                            !contentType.equals("image/png")
                            &&
                            !contentType.equals("image/webp"))) {

                throw new RuntimeException(
                        "Invalid image type");
            }

            // FILE EXTENSION
            String originalFilename = file.getOriginalFilename();

            String extension = originalFilename.substring(
                    originalFilename.lastIndexOf("."));

            // UNIQUE KEY
            String fileKey = "profiles/"
                    + staff.getId()
                    + "/"
                    + UUID.randomUUID()
                    + extension;

            // UPLOAD TO BACKBLAZE
            s3Client.putObject(

                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileKey)
                            .contentType(contentType)
                            .build(),

                    RequestBody.fromBytes(
                            file.getBytes()));

            // GENERATE FILE URL
            String fileUrl = endpoint
                    + "/"
                    + bucketName
                    + "/"
                    + fileKey;

            // SAVE IN DB
            staff.setProfilePhotoUrl(fileUrl);

            staff.setProfilePhotoKey(fileKey);

            staffDao.save(staff);

            return fileUrl;

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to upload profile photo");
        }
    }
    @Override
public ResponseEntity<byte[]> getProfilePhoto(
        String staffEmail
) {

    try {

        Staff staff =
                staffDao.getByEmail(
                        staffEmail
                );

        if (
                staff.getProfilePhotoKey()
                        == null
        ) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        GetObjectRequest request =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(
                                staff.getProfilePhotoKey()
                        )
                        .build();

        ResponseBytes<GetObjectResponse>
                objectBytes =
                s3Client.getObjectAsBytes(
                        request
                );

        String contentType =
                objectBytes.response()
                        .contentType();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_TYPE,
                        contentType
                )
                .body(
                        objectBytes.asByteArray()
                );

    } catch (Exception ex) {

        return ResponseEntity
                .internalServerError()
                .build();
    }
}
}