package com.skilllink.controller;

import com.skilllink.model.User;
import com.skilllink.repository.UserRepository;
import com.skilllink.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling user sign-up.
 * Accepts user data, hashes the password, saves the user, and returns a JWT.
 *
 * Author: Hrittija Bhattacharjee
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class SignupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public SignupController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user and returns a JWT if successful.
     *
     * @param user the user details
     * @return a success message and also a JWT token or an error message
     */
    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            String normalizedEmail = user.getEmail().trim().toLowerCase();
            user.setEmail(normalizedEmail);

            String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
            user.setPasswordHash(hashedPassword);

            userRepository.saveUser(user);

            String token = jwtUtil.generateToken(normalizedEmail);
            response.put("token", token);
            response.put("message", "Signup successful!");
        } catch (Exception e) {
            response.put("message", "Signup failed: " + e.getMessage());
        }
        return response;
    }
}
