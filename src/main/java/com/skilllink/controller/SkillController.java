package com.skilllink.controller;

import com.skilllink.model.SkillPreference;
import com.skilllink.repository.SkillPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public String addSkill(@RequestBody SkillPreference skill) {
        skill.setId(UUID.randomUUID().toString()); // auto-generate ID
        skillRepository.saveSkill(skill);
        return "Skill added successfully!";
    }

    // ⭐ NEW endpoint to get all skills
    @GetMapping("/all")
    public List<SkillPreference> getAllSkills() {
        return skillRepository.getAllSkills();
    }
}
