package com.example.everyhanghae.service;
import com.example.everyhanghae.dto.request.BoardRequestDto;
import com.example.everyhanghae.dto.response.BoardResposeDto;
import com.example.everyhanghae.dto.response.BoardTypeResponseDto;
import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.BoardType;
import com.example.everyhanghae.entity.User;
import com.example.everyhanghae.exception.CustomException;
import com.example.everyhanghae.repository.BoardRepository;
import com.example.everyhanghae.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.everyhanghae.exception.CustomErrorCode.NOT_AUTHOR;
import static com.example.everyhanghae.exception.CustomErrorCode.POST_NOT_FOUND;

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
    @javax.transaction.Transactional
    public void updatePost(Long boardId, BoardRequestDto boardRequestDto, User user) {
        Board board = isExistBoard(boardId);
        isAuthor(board,user);

//        게시글 작성자 일치여부 확인 > 공통메서드 처리
//        if (!board.getUser().getId().equals(user.getId())) {
//            throw new CustomException(NOT_AUTHOR);
//        }

        // 게시글 수정
        board.update(boardRequestDto.getTitle(), boardRequestDto.getContent());
        boardRepository.save(board);


    }

    public void deletePost(Long boardId, User user) {
        Board board = isExistBoard(boardId);
        isAuthor(board,user);
        boardRepository.deleteById(boardId);

    }


    //게시글 존재 하는지 확인 하는 공통 메서드
    public Board isExistBoard(Long id){
        return boardRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );
    }

    //작성자가 일치 하는지 확인 하는 공통 메서드
    public void isAuthor(Board board, User user) {
        if (!board.getUser().getId().equals(user.getId())) {
            throw new CustomException(NOT_AUTHOR);
        }
    }
}
