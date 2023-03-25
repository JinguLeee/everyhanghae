package com.example.everyhanghae.repository;

import com.example.everyhanghae.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
