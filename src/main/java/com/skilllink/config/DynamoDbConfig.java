package com.skilllink.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;

/**
 * DynamoDB Configuration for SkillLink
 *
 * This configuration sets up the AWS DynamoDB clients that my application uses to interact with the database.
 * 
 * - The first bean sets up the low-level {@link DynamoDbClient}, which talks directly to DynamoDB.
 * - The second bean sets up the {@link DynamoDbEnhancedClient}, which makes it easier to work with data using 
 *   Java objects instead of raw queries.
 *
 * Currently using the **us-east-2 (Ohio)** AWS region. If you're deploying to another region, you’ll want 
 * to update that here.
 *
 *
 * Author: Hrittija Bhattacharjee
 */
@Configuration
public class DynamoDbConfig {

    /**
     * Sets up the basic DynamoDB client.
     * This client handles all the low-level communication with AWS DynamoDB.
     *
     * @return a configured DynamoDbClient for the US East (Ohio) region
     */
    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_2) // Ohio region
                .build();
    }

    /**
     * Sets up the enhanced DynamoDB client.
     * This wraps the base client and gives a higher-level interface to work with mapped Java objects.
     *
     * @param dynamoDbClient the base DynamoDB client
     * @return a DynamoDB client
     */
    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}
