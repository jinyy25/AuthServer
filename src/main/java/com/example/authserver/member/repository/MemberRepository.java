package com.example.authserver.member.repository;

import com.example.authserver.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
}
