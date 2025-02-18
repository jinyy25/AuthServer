package com.example.authserver.member.repository;


import com.example.authserver.member.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {


}

