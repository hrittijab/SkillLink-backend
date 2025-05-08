package com.skilllink.controller;

import com.skilllink.model.User;
import com.skilllink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public String login(@RequestBody User loginRequest) {
        try {
            User existingUser = userRepository.getUserByEmail(loginRequest.getEmail());

            if (existingUser == null) {
                return "Login failed: User not found";
            }

            // USE passwordEncoder.matches instead of equals
            if (!passwordEncoder.matches(loginRequest.getPasswordHash(), existingUser.getPasswordHash())) {
                return "Login failed: Incorrect password";
            }

            return "Login successful!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Login failed: " + e.getMessage();
        }
    }
}
