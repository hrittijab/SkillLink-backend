package com.skilllink.controller;

import com.skilllink.model.User;
import com.skilllink.repository.UserRepository;
import com.skilllink.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles login requests and issues JWT tokens upon successful authentication.
 * Author: Hrittija Bhattacharjee
 */
@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticates the user using email and password.
     * If valid, returns a JWT token in the response body.
     *
     * @param loginRequest the user's email and password
     * @return JWT Token
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> login(@RequestBody User loginRequest) {
        Map<String, String> response = new HashMap<>();
        String normalizedEmail = loginRequest.getEmail().trim().toLowerCase();

        try {
            User existingUser = userRepository.getUserByEmail(normalizedEmail);

            if (existingUser == null ||
                !passwordEncoder.matches(loginRequest.getPasswordHash(), existingUser.getPasswordHash())) {
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String token = jwtUtil.generateToken(normalizedEmail);
            response.put("message", "Login successful!");
            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
