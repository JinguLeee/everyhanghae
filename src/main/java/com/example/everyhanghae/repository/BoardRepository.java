package com.example.everyhanghae.repository;
import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 해당 기수의 게시글만 불러오기
    List<Board> findAllByClassIdAndBoardTypeOrderByCreatedAtDesc(int classId, BoardType boardType);

}
