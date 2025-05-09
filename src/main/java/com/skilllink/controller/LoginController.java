package com.skilllink.controller;

import com.skilllink.model.User;
import com.skilllink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> login(@RequestBody User loginRequest) {
        Map<String, String> response = new HashMap<>();

        try {
            User existingUser = userRepository.getUserByEmail(loginRequest.getEmail());

            if (existingUser == null || !passwordEncoder.matches(loginRequest.getPasswordHash(), existingUser.getPasswordHash())) {
                response.put("message", "Invalid credentials");
                return response;
            }

            response.put("message", "Login successful!");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Login failed: " + e.getMessage());
            return response;
        }
    }
}
