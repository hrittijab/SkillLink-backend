package com.skilllink.controller;

import com.skilllink.model.User;
import com.skilllink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final UserRepository userRepository;

    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/setup")
    public String saveProfile(@RequestBody User updatedUser) {
        try {
            User existingUser = userRepository.getUserByEmail(updatedUser.getEmail());
            if (existingUser == null) {
                return "Profile setup failed: User not found";
            }

            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setBio(updatedUser.getBio());
            existingUser.setSkillsOffered(updatedUser.getSkillsOffered());
            existingUser.setSkillsWanted(updatedUser.getSkillsWanted());

            userRepository.updateUser(existingUser); // ⭐ Make sure your repository has updateUser method

            return "Profile updated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Profile setup failed: " + e.getMessage();
        }
    }
}
