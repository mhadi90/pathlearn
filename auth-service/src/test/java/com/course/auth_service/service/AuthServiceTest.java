package com.course.auth_service.service;


import com.course.auth_service.dto.*;
import com.course.auth_service.entity.User;
import com.course.auth_service.repository.TokenBlacklistRepository;
import com.course.auth_service.repository.UserRepository;
import com.course.auth_service.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenBlacklistRepository blacklistRepo;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setMdp("hashedpassword");
        testUser.setNom("Doe");
        testUser.setPrenom("John");
        testUser.setRole(User.Role.APPRENTI);
    }


    @Test
    void register_success() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setPassword("password123");
        request.setNom("New");
        request.setPrenom("User");
        request.setRole("APPRENTI");

        when(repo.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(repo.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("fake-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("fake-token", response.getToken());
        assertEquals("APPRENTI", response.getRole());
        verify(repo).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throws() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setNom("Test");
        request.setPrenom("User");
        request.setRole("APPRENTI");

        when(repo.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
        verify(repo, never()).save(any());
    }

    @Test
    void register_adminRole_defaultsToApprenti() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("admin@example.com");
        request.setPassword("password123");
        request.setNom("Admin");
        request.setPrenom("Sneaky");
        request.setRole("ADMIN");

        when(repo.existsByEmail("admin@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(repo.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertEquals("APPRENTI", response.getRole());
    }

    @Test
    void register_invalidRole_defaultsToApprenti() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("bad@example.com");
        request.setPassword("password123");
        request.setNom("Bad");
        request.setPrenom("Role");
        request.setRole("INVALID_ROLE");

        when(repo.existsByEmail("bad@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(repo.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
    }


    @Test
    void login_success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(repo.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedpassword")).thenReturn(true);
        when(jwtUtil.generateToken(testUser)).thenReturn("login-token");

        AuthResponse response = authService.login(request);

        assertEquals("login-token", response.getToken());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void login_wrongPassword_throws() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong");

        when(repo.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrong", "hashedpassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }

    @Test
    void login_nonExistentEmail_throws() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ghost@example.com");
        request.setPassword("password123");

        when(repo.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }

    // ===== LOGOUT =====

    @Test
    void logout_success() {
        String token = "valid-token";
        Claims claims = mock(Claims.class);

        when(blacklistRepo.existsByToken(token)).thenReturn(false);
        when(jwtUtil.parseToken(token)).thenReturn(claims);
        when(claims.getExpiration()).thenReturn(new java.util.Date(System.currentTimeMillis() + 86400000));

        authService.logout(token);

        verify(blacklistRepo).save(any());
    }

    @Test
    void logout_alreadyBlacklisted_throws() {
        String token = "blacklisted-token";

        when(blacklistRepo.existsByToken(token)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.logout(token));
    }




    @Test
    void getProfile_blacklistedToken_throws() {
        when(blacklistRepo.existsByToken("bad-token")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.getProfile("bad-token"));
    }

    @Test
    void getProfile_userNotFound_throws() {
        String token = "valid-token";
        Claims claims = mock(Claims.class);

        when(blacklistRepo.existsByToken(token)).thenReturn(false);
        when(jwtUtil.parseToken(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("999");
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.getProfile(token));
    }




    @Test
    void updateProfile_duplicateEmail_throws() {
        String token = "valid-token";
        Claims claims = mock(Claims.class);
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setEmail("taken@example.com");

        when(blacklistRepo.existsByToken(token)).thenReturn(false);
        when(jwtUtil.parseToken(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");
        when(repo.findById(1L)).thenReturn(Optional.of(testUser));
        when(repo.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.updateProfile(token, request));
    }
}