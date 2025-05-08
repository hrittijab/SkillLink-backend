package com.skilllink.repository;

import com.skilllink.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @PostConstruct
    public void init() {
        this.userTable = enhancedClient.table("SkillLinkUsers", TableSchema.fromBean(User.class));
    }

    public void saveUser(User user) {
        userTable.putItem(user);
    }

    public User getUserByEmail(String email) {
        return userTable.getItem(r -> r.key(k -> k.partitionValue(email)));
    }

    public void updateUser(User user) {
        userTable.updateItem(user);
    }
}
