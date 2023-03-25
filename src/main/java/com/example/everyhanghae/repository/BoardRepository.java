package com.example.everyhanghae.repository;
import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 해당 기수의 게시글만 불러오기
    @Query(value = "select a from Board a where a.classId = :classId and a.boardType = :boardType")
    List<Board> queryFindAllByClassId(@Param("classId") int classId, @Param("boardType") int boardType);

    // 게시물 종류 조회
    @Query(value = "select b from BoardType b")
    List<BoardType> queryFindAll();


}
