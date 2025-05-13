package com.skilllink.repository;

import com.skilllink.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * Repository for managing user data in DynamoDB.
 * Handles basic CRUD operations on SkillLinkUsers table.
 *
 * Author: Hrittija Bhattacharjee
 */
@Repository
public class UserRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    /**
     * Initializes the user table after bean construction.
     */
    @PostConstruct
    public void init() {
        this.userTable = enhancedClient.table("SkillLinkUsers", TableSchema.fromBean(User.class));
    }

    /**
     * Saves a new user to the database.
     * @param user the user to save
     */
    public void saveUser(User user) {
        userTable.putItem(user);
    }

    /**
     * Retrieves a user by their email address.
     * @param email the user's email
     * @return that certain User object or null if not found
     */
    public User getUserByEmail(String email) {
        return userTable.getItem(r -> r.key(k -> k.partitionValue(email)));
    }

    /**
     * Updates an existing user's data.
     * @param user the updated user object
     */
    public void updateUser(User user) {
        userTable.updateItem(user);
    }

    /**
     * Deletes a user from the database by their email address.
     * @param email the email of the user to delete
     */
    public void deleteUserByEmail(String email) {
        userTable.deleteItem(r -> r.key(k -> k.partitionValue(email)));
    }
} 
