package com.course.auth_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_token")
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    @Column(name = "blacklisted_at")
    private LocalDateTime blacklistedAt;

    @PrePersist
    void onCreate() { this.blacklistedAt = LocalDateTime.now(); }

    public BlacklistedToken() {}

    public BlacklistedToken(String token, LocalDateTime expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public LocalDateTime getExpiration() { return expiration; }
    public LocalDateTime getBlacklistedAt() { return blacklistedAt; }
}