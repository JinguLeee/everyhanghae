package com.example.everyhanghae.repository;

import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.Likes;
import com.example.everyhanghae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    //보드, 사용자 확인
    Optional<Likes> findByBoardAndUser(Board borard, User user);

    boolean existsByBoardAndUser(Board borard, User user);

    //공감 넣기
    int countByBoard(Board board);

    void deleteAllByBoard(Board board);

//    //게시글 공감 전체 조회
//    @Query(value = "select count(*) from Likes c where c.board_Id.id = :boardId and c.select = true")
//    int countByBoardIdAndSelectIsTrue(@Param("boardId") Long boardId);


}
