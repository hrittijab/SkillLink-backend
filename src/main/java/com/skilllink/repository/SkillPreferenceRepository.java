package com.skilllink.repository;

import com.skilllink.model.SkillPreference;
import java.util.List;
/**
 * Repository interface for managing skill preferences.
 * Defines methods for saving, retrieving, and deleting skill posts in the application.
 *
 * Author: Hrittija Bhattacharjee
 */
public interface SkillPreferenceRepository {
    void saveSkill(SkillPreference skill);
    List<SkillPreference> getAllSkills(); 
    void deleteSkillById(String id);
    SkillPreference getSkillById(String id);


}
