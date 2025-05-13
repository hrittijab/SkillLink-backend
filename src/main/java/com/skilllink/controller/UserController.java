package com.skilllink.controller;

import com.skilllink.model.User;
import com.skilllink.repository.UserRepository;
import com.skilllink.requests.PasswordChangeRequest;
import com.skilllink.requests.PasswordVerifyRequest;
import com.skilllink.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private S3Service s3Service;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        String normalizedEmail = email.trim().toLowerCase();
        User user = userRepository.getUserByEmail(normalizedEmail);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        String normalizedEmail = updatedUser.getEmail().trim().toLowerCase();
        User existingUser = userRepository.getUserByEmail(normalizedEmail);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        updatedUser.setEmail(normalizedEmail);
        updatedUser.setPasswordHash(existingUser.getPasswordHash());

        userRepository.updateUser(updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@RequestBody PasswordVerifyRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        User user = userRepository.getUserByEmail(normalizedEmail);
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.ok("Password verified");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password incorrect");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest req) {
        String normalizedEmail = req.getEmail().trim().toLowerCase();
        User user = userRepository.getUserByEmail(normalizedEmail);
        if (user != null && passwordEncoder.matches(req.getOldPassword(), user.getPasswordHash())) {
            user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
            userRepository.updateUser(user);
            return ResponseEntity.ok("Password changed successfully");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password incorrect");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String email) {
        String normalizedEmail = email.trim().toLowerCase();
        userRepository.deleteUserByEmail(normalizedEmail);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/profile/setup")
    public ResponseEntity<String> setupProfile(@RequestBody User user) {
        String normalizedEmail = user.getEmail().trim().toLowerCase();
        User existing = userRepository.getUserByEmail(normalizedEmail);

        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setBio(user.getBio());
        existing.setSkillsOffered(user.getSkillsOffered());
        existing.setSkillsWanted(user.getSkillsWanted());

        if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
            existing.setProfilePictureUrl(user.getProfilePictureUrl());
        }

        userRepository.updateUser(existing);
        return ResponseEntity.ok("Profile setup successful");
    }

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam("email") String email,
            @RequestParam("file") MultipartFile file) {

        try {
            String normalizedEmail = email.trim().toLowerCase();
            String url = s3Service.uploadProfilePicture(normalizedEmail, file);

            User user = userRepository.getUserByEmail(normalizedEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            user.setProfilePictureUrl(url);
            userRepository.updateUser(user);

            return ResponseEntity.ok(url);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image");
        }
    }
}
