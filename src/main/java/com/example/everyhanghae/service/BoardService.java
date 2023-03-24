package com.example.everyhanghae.service;
import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.BoardEnum;
import com.example.everyhanghae.entity.User;
import com.example.everyhanghae.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 전체 게시글 조회
    public void getAllBoards(User user) {
        Long classId = 0L;
        int boardType = BoardEnum.NOTICE_BOARD.getBoardNumber();
        List<Board> boardList = boardRepository.queryFindAllByClassId(classId, boardType);
        boardType = BoardEnum.BIN_BOARD.getBoardNumber();

    }
}
