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
    public SkillPreference getSkillById(String id) {
    return skillTable.getItem(Key.builder().partitionValue(id).build());
}


    @Override
    public void saveSkill(SkillPreference skill) {
        skillTable.putItem(skill);
    }

    @Override
    public List<SkillPreference> getAllSkills() {
        try {
            List<SkillPreference> items = skillTable.scan().items().stream().collect(Collectors.toList());
            System.out.println("✅ DynamoDB scan successful: Retrieved " + items.size() + " skill(s)");
            for (SkillPreference skill : items) {
                System.out.println("🔹 Skill: " + skill.getSkillName() + " | ID: " + skill.getId());
            }
            return items;
        } catch (Exception e) {
            System.out.println("❌ DynamoDB scan failed: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public void deleteSkillById(String id) {
        skillTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }

}
