package com.skilllink.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class SkillPreference {

    private String id; // random UUID
    private String userEmail; // to link to user
    private String skillName;
    private String preferenceType; // TEACH or LEARN
    private String paymentType;    // FREE, PAID, EXCHANGE
    private Double price;           // optional if PAID
    private String exchangeSkill;   // optional if EXCHANGE

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

    public String getExchangeSkill() {
        return exchangeSkill;
    }

    public void setExchangeSkill(String exchangeSkill) {
        this.exchangeSkill = exchangeSkill;
    }
}
