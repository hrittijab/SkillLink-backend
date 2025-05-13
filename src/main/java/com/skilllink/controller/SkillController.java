package com.skilllink.controller;

import com.skilllink.model.SkillPreference;
import com.skilllink.model.User;
import com.skilllink.repository.SkillPreferenceRepository;
import com.skilllink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for managing skill posts in SkillLink.
 * Handles skill creation, updates, deletion, and retrieval.
 *
 * Author: Hrittija Bhattacharjee
 */
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

    /**
     * Adds a new skill post.
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addSkill(@RequestBody SkillPreference skill) {
        Map<String, String> response = new HashMap<>();
        try {
            skill.setId(UUID.randomUUID().toString());
            skill.setCreatedAt(Instant.now().toString());

            if (skill.getUserEmail() != null) {
                skill.setUserEmail(skill.getUserEmail().trim().toLowerCase());
            }

            skill.setStatus("ACTIVE");
            skillRepository.saveSkill(skill);

            response.put("message", "Skill added successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to save skill: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves all skills posted by a specific user.
     */
    @GetMapping("/user")
    public ResponseEntity<List<SkillPreference>> getSkillsByUserEmail(@RequestParam String email) {
        String normalizedEmail = email.trim().toLowerCase();
        List<SkillPreference> userSkills = skillRepository.getAllSkills()
            .stream()
            .filter(skill -> skill.getUserEmail() != null && skill.getUserEmail().equalsIgnoreCase(normalizedEmail))
            .collect(Collectors.toList());
        return ResponseEntity.ok(userSkills);
    }

    /**
     * Deletes a skill post by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable String id) {
        skillRepository.deleteSkillById(id);
        return ResponseEntity.ok("Skill deleted");
    }

    /**
     * Updates an existing skill post.
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateSkill(@RequestBody SkillPreference updatedSkill) {
        if (updatedSkill.getUserEmail() != null) {
            updatedSkill.setUserEmail(updatedSkill.getUserEmail().trim().toLowerCase());
        }
        skillRepository.saveSkill(updatedSkill);
        return ResponseEntity.ok("Skill updated");
    }

    /**
     * Retrieves a single skill post by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SkillPreference> getSkillById(@PathVariable String id) {
        SkillPreference skill = skillRepository.getSkillById(id);
        if (skill != null) {
            return ResponseEntity.ok(skill);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Retrieves all skill posts and fills them with user profile information.
     */
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
            item.put("status", skill.getStatus());

            String normalizedEmail = skill.getUserEmail() != null
                    ? skill.getUserEmail().trim().toLowerCase()
                    : null;

            User user = userRepository.getUserByEmail(normalizedEmail);
            if (user != null) {
                item.put("firstName", user.getFirstName());
                item.put("lastName", user.getLastName());
                item.put("profilePictureUrl", user.getProfilePictureUrl());
            }

            enriched.add(item);
        }

        return enriched;
    }
}
