package com.skilllink.controller;

import com.skilllink.model.SkillPreference;
import com.skilllink.repository.SkillPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillController {

    private final SkillPreferenceRepository skillRepository;

    @Autowired
    public SkillController(SkillPreferenceRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addSkill(@RequestBody SkillPreference skill) {
        Map<String, String> response = new HashMap<>();
        try {
            skill.setId(UUID.randomUUID().toString());
            skillRepository.saveSkill(skill);
            response.put("message", "Skill added successfully!");
            return ResponseEntity.ok(response);  // ✅ returns 200 + JSON
        } catch (Exception e) {
            response.put("message", "Failed to save skill: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  
        }
    }


    @GetMapping("/all")
    public List<SkillPreference> getAllSkills() {
        return skillRepository.getAllSkills();
    }
}
