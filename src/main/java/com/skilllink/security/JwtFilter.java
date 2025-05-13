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
    System.out.println("🔍 Request URI: " + path);

    // ✅ Skip JWT check for public endpoints
    if (path.equals("/api/signup") ||
        path.equals("/api/login") ||
        path.equals("/api/messages/previews") ||
        path.equals("/api/messages/conversations") ||
        path.equals("/api/messages/conversation") ||
        path.startsWith("/api/skills") ||
        path.startsWith("/chat") || 
        path.startsWith("/topic") || 
        path.startsWith("/app")) {

        System.out.println("🛑 Skipping JWT validation for: " + path);
        filterChain.doFilter(request, response);
        return;
    }

    String authHeader = request.getHeader("Authorization");
    System.out.println("🔑 Authorization Header: " + authHeader);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        if (jwtUtil.isTokenValid(token)) {
            String email = jwtUtil.extractEmail(token);
            User user = userRepository.getUserByEmail(email);

            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, null);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ Authenticated user: " + email);
            }
        } else {
            System.out.println("❌ Invalid JWT token.");
        }
    } else {
        System.out.println("⚠️ No Authorization header found.");
    }

    filterChain.doFilter(request, response);
}



}
