package com.example.everyhanghae.repository;

import com.example.everyhanghae.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardTypeRepository extends JpaRepository<BoardType, Integer> {

    // 게시물 종류 전체 조회
    @Query(value = "select b from BoardType b")
    List<BoardType> queryFindAll();

    // 게시물 종류 타입별 조회
    @Query(value = "select b from BoardType b where b.boardType = :boardType")
    Optional<BoardType> queryFindByType(@Param("boardType") int boardType);


}
