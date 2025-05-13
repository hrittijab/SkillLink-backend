package com.skilllink.security;

import com.skilllink.model.User;
import com.skilllink.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT filter that validates incoming requests and sets authentication context.
 * Skips validation for public routes like login, signup, and skill/message browsing.
 *
 * Author: Hrittija Bhattacharjee
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip JWT validation for public routes
        if (path.equals("/api/signup") ||
            path.equals("/api/login") ||
            path.equals("/api/messages/previews") ||
            path.equals("/api/messages/conversations") ||
            path.equals("/api/messages/conversation") ||
            path.startsWith("/api/skills") ||
            path.startsWith("/chat") ||
            path.startsWith("/topic") ||
            path.startsWith("/app")) {

            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Validate token and extract email
            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);
                User user = userRepository.getUserByEmail(email);

                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, null);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
} 