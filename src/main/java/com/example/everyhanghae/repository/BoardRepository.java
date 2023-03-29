package com.example.everyhanghae.repository;
import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.BoardType;
import com.example.everyhanghae.entity.Comment;
import com.example.everyhanghae.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 해당 기수의 게시글만 타입별로 불러오기
//    @Query(value = "select b , t from Board b, BoardType t where b.classId = :classId and b.boardType = :boardType and b.boardType.boardType = t.boardType")
//    List<Board> findAllByClassIdAndBoardTypeOrderByCreatedAtDesc(@Param("classId") int classId, @Param("boardType") int boardType);
    List<Board> findAllByClassIdAndBoardTypeOrderByCreatedAtDesc(int classId, BoardType boardType);

    // 페이징처리
    Page<Board> findAllByClassIdAndBoardType(int classId, BoardType boardType, Pageable pageable);

    List<Board> findAllByUser(User user);

}
