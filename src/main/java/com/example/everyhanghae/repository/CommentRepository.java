package com.example.everyhanghae.repository;

import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.Comment;
import com.example.everyhanghae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    //쿼리 작성?
    List<Comment> findByBoardId(Long boardId);

    int countByBoard(Board board);

    void deleteAllByBoard(Board board);
    void deleteAllByUser(User user);
}
