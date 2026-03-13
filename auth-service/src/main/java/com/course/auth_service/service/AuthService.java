package com.course.auth_service.service;


import com.course.auth_service.dto.*;
import com.course.auth_service.entity.BlacklistedToken;
import com.course.auth_service.entity.User;
import com.course.auth_service.repository.TokenBlacklistRepository;
import com.course.auth_service.repository.UserRepository;
import com.course.auth_service.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class AuthService {

    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Add to constructor
    private final TokenBlacklistRepository blacklistRepo;

    public AuthService(UserRepository repo, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, TokenBlacklistRepository blacklistRepo) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.blacklistRepo = blacklistRepo;
    }

// Add these methods

    public UserProfileResponse getProfile(String token) {
        checkBlacklist(token);
        Claims claims = jwtUtil.parseToken(token);
        Long userId = Long.valueOf(claims.getSubject());
        User user = repo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserProfileResponse(
                user.getId(), user.getEmail(), user.getNom(),
                user.getPrenom(), user.getRole().name(),
                user.getDateCreation().toString()
        );
    }

    public UserProfileResponse updateProfile(String token, UpdateProfileRequest request) {
        checkBlacklist(token);
        Claims claims = jwtUtil.parseToken(token);
        Long userId = Long.valueOf(claims.getSubject());
        User user = repo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getNom() != null) user.setNom(request.getNom());
        if (request.getPrenom() != null) user.setPrenom(request.getPrenom());
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(user.getEmail()) && repo.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }

        User saved = repo.save(user);
        return new UserProfileResponse(
                saved.getId(), saved.getEmail(), saved.getNom(),
                saved.getPrenom(), saved.getRole().name(),
                saved.getDateCreation().toString()
        );
    }

    public void logout(String token) {
        checkBlacklist(token);
        if (blacklistRepo.existsByToken(token)) {
            throw new IllegalArgumentException("Token already invalidated");
        }
        Claims claims = jwtUtil.parseToken(token);
        LocalDateTime expiration = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(claims.getExpiration().getTime()), ZoneId.systemDefault()
        );
        blacklistRepo.save(new BlacklistedToken(token, expiration));
    }

    public AuthResponse register(RegisterRequest request) {
        if (repo.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setMdp(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());

        // Default to APPRENTI if no role given, never allow ADMIN via register
        User.Role role;
        try {
            role = User.Role.valueOf(request.getRole().toUpperCase());
            if (role == User.Role.ADMIN) role = User.Role.APPRENTI; // security guard
        } catch (Exception e) {
            role = User.Role.APPRENTI;
        }
        user.setRole(role);

        User saved = repo.save(user);
        String token = jwtUtil.generateToken(saved);
        return new AuthResponse(token, saved.getRole().name(), saved.getEmail(), saved.getId());
    }

    public AuthResponse login(LoginRequest request) {
        User user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getMdp())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getRole().name(), user.getEmail(), user.getId());
    }

    private void checkBlacklist(String token) {
        if (blacklistRepo.existsByToken(token)) {
            throw new IllegalArgumentException("Token has been invalidated");
        }
    }
}
