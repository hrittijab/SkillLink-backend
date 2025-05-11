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

    @PostMapping
    public ResponseEntity<Map<String, String>> login(@RequestBody User loginRequest) {
        Map<String, String> response = new HashMap<>();
        String normalizedEmail = loginRequest.getEmail().trim().toLowerCase();
        System.out.println("🔐 Login attempt for: " + normalizedEmail);

        try {
            User existingUser = userRepository.getUserByEmail(normalizedEmail);

            if (existingUser == null) {
                System.out.println("❌ User not found for email: " + normalizedEmail);
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            boolean passwordMatch = passwordEncoder.matches(loginRequest.getPasswordHash(), existingUser.getPasswordHash());
            if (!passwordMatch) {
                System.out.println("❌ Password mismatch for user: " + normalizedEmail);
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String token = jwtUtil.generateToken(normalizedEmail);
            System.out.println("✅ Login successful. JWT issued for: " + normalizedEmail);
            System.out.println("🪙 JWT Token: " + token);

            response.put("message", "Login successful!");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("🔥 Login failed due to exception: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
