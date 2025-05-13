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

    @PostConstruct
    public void init() {
        System.out.println("🪣 S3 Service Initialized:");
        System.out.println("   - Bucket: " + bucket);
        System.out.println("   - Region: " + region);
        System.out.println("   - AccessKey present: " + (accessKey != null));
        System.out.println("   - SecretKey present: " + (secretKey != null));

        s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    public String uploadProfilePicture(String email, MultipartFile file) throws IOException {
    String sanitizedEmail = email.replace("@", "_"); // Optional: avoid @ in key
    String key = "profile-pics/" + sanitizedEmail + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
    System.out.println("⬆️ Uploading to S3 → key: " + key);

    try {
        s3.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build(), 
            RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );


        String url = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
        System.out.println("✅ Upload success. File URL: " + url);
        return url;
    } catch (Exception e) {
        System.err.println("❌ Upload to S3 failed: " + e.getMessage());
        throw new RuntimeException("S3 upload failed", e);
    }
}
}
