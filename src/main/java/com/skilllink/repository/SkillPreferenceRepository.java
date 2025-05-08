package com.skilllink.repository;

import com.skilllink.model.SkillPreference;
import java.util.List;

public interface SkillPreferenceRepository {
    void saveSkill(SkillPreference skill);
    List<SkillPreference> getAllSkills(); // ⭐ added
}
