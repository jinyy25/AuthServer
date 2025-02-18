package com.example.authserver.security.repository;

import com.example.authserver.security.entity.Auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, String> {
    Optional<Auth> findByRefreshToken(String refreshToken);
    boolean existsByUserId(String userId);
    Optional<Auth> findByUserId(String userId);
}
