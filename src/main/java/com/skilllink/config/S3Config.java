package com.skilllink.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Configures the AWS S3 client profile images in SkillLink application.
 *
 * Values like access key, secret key, and region are injected from here
 *
 * Author: Hrittija Bhattacharjee
 */
@Configuration
public class S3Config {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    /**
     * Creates and configures the S3 client with credentials and region.
     *
     * @return a configured client
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                    StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                    )
                )
                .build();
    }
}
