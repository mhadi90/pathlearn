package com.course.auth_service.repository;

import com.course.auth_service.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);
}