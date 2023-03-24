package com.example.everyhanghae.repository;

import com.example.everyhanghae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByUsername(String userName);
}