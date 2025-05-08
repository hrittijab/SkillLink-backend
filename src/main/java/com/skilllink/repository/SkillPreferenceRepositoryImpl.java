package com.skilllink.repository;

import com.skilllink.model.SkillPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SkillPreferenceRepositoryImpl implements SkillPreferenceRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<SkillPreference> skillTable;

    @Autowired
    public SkillPreferenceRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @PostConstruct
    public void init() {
        skillTable = enhancedClient.table("SkillPreference", software.amazon.awssdk.enhanced.dynamodb.TableSchema.fromBean(SkillPreference.class));
    }

    @Override
    public void saveSkill(SkillPreference skill) {
        skillTable.putItem(skill);
    }

    @Override
    public List<SkillPreference> getAllSkills() {
        return skillTable.scan().items().stream().collect(Collectors.toList());
    }
}
