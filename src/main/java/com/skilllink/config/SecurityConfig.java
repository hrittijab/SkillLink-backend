package com.skilllink.config;

import com.skilllink.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the SkillLink backend.
 *
 * This class sets up password encoding, JWT filtering, and HTTP route access rules.
 * Author: Hrittija Bhattacharjee
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Used during user sign-up and login for secure password handling.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures security rules and applies JWT filtering to all protected endpoints.
     * 
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/signup",
                    "/api/login",
                    "/api/skills/**",
                    "/api/messages/**",
                    "/chat/**",     // STOMP/WebSocket endpoint
                    "/topic/**",    // Message topic
                    "/app/**"       // Message sending
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Provides the authentication manager used to authenticate login credentials.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
