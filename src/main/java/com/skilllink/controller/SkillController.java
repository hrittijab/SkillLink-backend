package com.skilllink.controller;

import com.skilllink.model.SkillPreference;
import com.skilllink.model.User;
import com.skilllink.repository.SkillPreferenceRepository;
import com.skilllink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillController {

    private final SkillPreferenceRepository skillRepository;
    private final UserRepository userRepository;

    @Autowired
    public SkillController(SkillPreferenceRepository skillRepository, UserRepository userRepository) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addSkill(@RequestBody SkillPreference skill) {
        Map<String, String> response = new HashMap<>();
        try {
            skill.setId(UUID.randomUUID().toString());
            skill.setCreatedAt(Instant.now().toString()); 
            skillRepository.saveSkill(skill);
            response.put("message", "Skill added successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to save skill: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/all")
    public List<Map<String, Object>> getAllSkills() {
        List<SkillPreference> skills = skillRepository.getAllSkills();
        List<Map<String, Object>> enriched = new ArrayList<>();

        for (SkillPreference skill : skills) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", skill.getId());
            item.put("userEmail", skill.getUserEmail());
            item.put("skillName", skill.getSkillName());
            item.put("preferenceType", skill.getPreferenceType());
            item.put("paymentType", skill.getPaymentType());
            item.put("price", skill.getPrice());
            item.put("exchangeSkills", skill.getExchangeSkills());
            item.put("createdAt", skill.getCreatedAt());

            User user = userRepository.getUserByEmail(skill.getUserEmail());
            if (user != null) {
                item.put("firstName", user.getFirstName());
                item.put("lastName", user.getLastName());
            }

            enriched.add(item);
        }

        return enriched;
    }
}
