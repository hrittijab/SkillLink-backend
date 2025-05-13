package com.skilllink.controller;

import com.skilllink.model.User;
import com.skilllink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing user profile setup and updates.
 *
 * Author: Hrittija Bhattacharjee
 */
@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final UserRepository userRepository;

    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Updates or sets up a user's profile based on provided details.
     *
     * @param updatedUser the user object with new profie details
     * @return message indicating success or failure
     */
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

            userRepository.updateUser(existingUser);

            return "Profile updated successfully!";
        } catch (Exception e) {
            return "Profile setup failed: " + e.getMessage();
        }
    }
}