package com.skilllink.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

/**
 * Service to handle interactions with AWS S3 for uploading profile pictures.
 *
 * Author: Hrittija Bhattacharjee
 */
@Service
public class S3Service {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.bucket}")
    private String bucket;

    private S3Client s3;

    /**
     * Initializes the S3 client after the service is constructed.
     */
    @PostConstruct
    public void init() {
        s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    /**
     * Uploads a profile picture to the configured S3 bucket.
     *
     * @param email the email of the user (used in the key name)
     * @param file  the profile picture file to upload
     * @return the public URL of the uploaded image
     * @throws IOException if reading the file input stream fails
     */
    public String uploadProfilePicture(String email, MultipartFile file) throws IOException {
        String sanitizedEmail = email.replace("@", "_");
        String key = "profile-pics/" + sanitizedEmail + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            s3.putObject(
                PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
        } catch (Exception e) {
            throw new RuntimeException("S3 upload failed", e);
        }
    }
} 
