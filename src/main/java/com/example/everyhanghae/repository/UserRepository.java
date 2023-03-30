package com.example.everyhanghae.repository;
import com.example.everyhanghae.entity.ClassCode;
import com.example.everyhanghae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByUsername(String userName);

    Optional<User> findByKakaoId(Long id);
    Optional<User> findByEmail(String email);

}