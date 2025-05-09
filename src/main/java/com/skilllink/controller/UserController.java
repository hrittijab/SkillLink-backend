package com.skilllink.controller;

import com.skilllink.model.User;
import com.skilllink.repository.UserRepository;
import com.skilllink.requests.PasswordChangeRequest;
import com.skilllink.requests.PasswordVerifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = userRepository.getUserByEmail(email);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        User existingUser = userRepository.getUserByEmail(updatedUser.getEmail());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        updatedUser.setPasswordHash(existingUser.getPasswordHash());

        userRepository.updateUser(updatedUser);
        return ResponseEntity.ok(updatedUser);
    }


    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@RequestBody PasswordVerifyRequest request) {
        User user = userRepository.getUserByEmail(request.getEmail());
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.ok("Password verified");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password incorrect");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest req) {
        User user = userRepository.getUserByEmail(req.getEmail());
        if (user != null && passwordEncoder.matches(req.getOldPassword(), user.getPasswordHash())) {
            user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
            userRepository.updateUser(user);
            return ResponseEntity.ok("Password changed successfully");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password incorrect");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String email) {
        userRepository.deleteUserByEmail(email);
        return ResponseEntity.ok("User deleted successfully");
    }
}
