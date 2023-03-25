package com.example.everyhanghae.service;
import com.example.everyhanghae.dto.request.BoardRequestDto;
import com.example.everyhanghae.dto.response.BoardResposeDto;
import com.example.everyhanghae.dto.response.BoardTypeResponseDto;
import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.BoardType;
import com.example.everyhanghae.entity.User;
import com.example.everyhanghae.repository.BoardRepository;
import com.example.everyhanghae.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 전체 게시글 조회
    @Transactional
    public List<BoardTypeResponseDto> getAllBoards(User user) {
        List<BoardTypeResponseDto> boardTypeResponseDtoList = new ArrayList<>();
        List<BoardType> boardTypeList = boardRepository.queryFindAll();
        for (BoardType boardType : boardTypeList) {
            List<Board> boardList = boardRepository.queryFindAllByClassId(user.getClassId(), boardType.getType());
            List<BoardResposeDto> boardResposeDtoList = new ArrayList<>();
            System.out.println("boardList.size() = " + boardList.size());
            for (Board board : boardList) {
                boardResposeDtoList.add(new BoardResposeDto(board));
            }
            boardTypeResponseDtoList.add(new BoardTypeResponseDto(boardType.getType(), boardResposeDtoList));
        }
        return boardTypeResponseDtoList;
    }

    @Transactional
    public void getTypeBoards(int boardType, User user) {
    }

    @Transactional
    public void getDetailBoards(Long boardId, UserDetailsImpl userDetails) {
    }

    @Transactional
    public void createPost(BoardRequestDto postRequestDto, User user) {
        boardRepository.saveAndFlush(new Board(postRequestDto, user));
    }
}
