package com.skilllink.repository;

import com.skilllink.model.SkillPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of SkillPreferenceRepository for DynamoDB.
 * Manages CRUD operations for skill preferences using DynamoDB client.
 *
 * Author: Hrittija Bhattacharjee
 */
@Repository
public class SkillPreferenceRepositoryImpl implements SkillPreferenceRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<SkillPreference> skillTable;

    @Autowired
    public SkillPreferenceRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    /**
     * Initializes the DynamoDB table reference after dependency injection.
     */
    @PostConstruct
    public void init() {
        skillTable = enhancedClient.table("SkillPreference", software.amazon.awssdk.enhanced.dynamodb.TableSchema.fromBean(SkillPreference.class));
    }

    /**
     * Retrieves a skill preference by ID.
     */
    @Override
    public SkillPreference getSkillById(String id) {
        return skillTable.getItem(Key.builder().partitionValue(id).build());
    }

    /**
     * Saves or updates a skill preference.
     */
    @Override
    public void saveSkill(SkillPreference skill) {
        skillTable.putItem(skill);
    }

    /**
     * Retrieves all skill preferences from the table.
     */
    @Override
    public List<SkillPreference> getAllSkills() {
        try {
            return skillTable.scan().items().stream().collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Deletes a skill preference by its ID.
     */
    @Override
    public void deleteSkillById(String id) {
        skillTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }
} 
