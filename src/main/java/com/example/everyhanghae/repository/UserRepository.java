package com.example.everyhanghae.repository;
import com.example.everyhanghae.entity.ClassCode;
import com.example.everyhanghae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByUsername(String userName);

    // 시크릿 코드 검색
    @Query(value = "SELECT c FROM ClassCode c WHERE c.secretCode = :secretCode")
    Optional<ClassCode> queryFindBySecret(@Param("secretCode") String secret);

}