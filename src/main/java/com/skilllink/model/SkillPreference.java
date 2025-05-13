package com.skilllink.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

/**
 * Represents a skill post in the SkillLink exploreSkills page.
 * This includes the skill name, user association, preference (teach/learn),
 * payment type.
 *
 * Author: Hrittija Bhattacharjee
 */
@DynamoDbBean
public class SkillPreference {

    private String id; // Unique identifier (UUID)
    private String userEmail; // Email of the user who created the post
    private String skillName; // Name of the skill
    private String preferenceType; // "TEACH" or "LEARN"
    private String paymentType;    // "FREE", "PAID", or "EXCHANGE"
    private Double price;           // Price (only relevant if paymentType is PAID)
    private List<String> exchangeSkills; // Skills user wants in exchange (if applicable)
    private String createdAt; // Timestamp string
    private String status; // Status of the post (ACTIVE, INACTIVE)

    public SkillPreference() {}

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getPreferenceType() {
        return preferenceType;
    }

    public void setPreferenceType(String preferenceType) {
        this.preferenceType = preferenceType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getExchangeSkills() {
        return exchangeSkills;
    }

    public void setExchangeSkills(List<String> exchangeSkills) {
        this.exchangeSkills = exchangeSkills;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}