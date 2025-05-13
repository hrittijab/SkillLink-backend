package com.skilllink.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * Represents a user profile in the SkillLink platform.
 * Includes user authentication data and optional profile information.
 *
 * Author: Hrittija Bhattacharjee
 */
@DynamoDbBean
public class User {

    private String email;               // User's email (used as the partition key)
    private String passwordHash;        // Hashed password for authentication
    private String firstName;           // First name of the user
    private String lastName;            // Last name of the user
    private String profilePictureUrl;   // Optional profile picture URL
    private String bio;                 // Short biography/description
    private String skillsOffered;       // Skills the user can teach
    private String skillsWanted;        // Skills the user wants to learn

    public User() {}

    public User(String email, String passwordHash, String firstName, String lastName, String profilePictureUrl, String bio, String skillsOffered, String skillsWanted) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.skillsOffered = skillsOffered;
        this.skillsWanted = skillsWanted;
    }

    @DynamoDbPartitionKey
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSkillsOffered() {
        return skillsOffered;
    }

    public void setSkillsOffered(String skillsOffered) {
        this.skillsOffered = skillsOffered;
    }

    public String getSkillsWanted() {
        return skillsWanted;
    }

    public void setSkillsWanted(String skillsWanted) {
        this.skillsWanted = skillsWanted;
    }
}