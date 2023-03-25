package com.example.everyhanghae.service;
import com.example.everyhanghae.dto.request.BoardRequestDto;
import com.example.everyhanghae.dto.response.BoardResponseDto;
import com.example.everyhanghae.dto.response.BoardResponseAllDto;
import com.example.everyhanghae.dto.response.BoardTypeResponseDto;
import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.BoardType;
import com.example.everyhanghae.entity.User;
import com.example.everyhanghae.exception.CustomErrorCode;
import com.example.everyhanghae.exception.CustomException;
import com.example.everyhanghae.repository.BoardRepository;
import com.example.everyhanghae.repository.BoardTypeRepository;
import com.example.everyhanghae.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardTypeRepository boardTypeRepository;

    // 게시글 전체, 유형별 조회
    @Transactional
    public List<BoardResponseAllDto> getTypeBoards(int boardType, User user) {
        System.out.println("boardType = " + boardType);
        List<BoardResponseAllDto> boardResponseAllDtoList = new ArrayList<>();
        Optional<BoardType> optionalBoardType = boardTypeRepository.queryFindByType(boardType);
        if (boardType == 0){
            // 반환할 리스트 선언, 게시글 타입별 리스트를 add 하여 보냄

            // 현재 게시판 타입을 모두 조회
            List<BoardType> boardTypeList = boardTypeRepository.queryFindAll();

            // 타입별로 게시판을 전체 조회
            for (BoardType boardTypeEntity : boardTypeList) {
                // 기수, 게시판 타입으로 조회
                List<Board> boardList = boardRepository.findAllByClassIdAndBoardTypeOrderByCreatedAtDesc(user.getClassId(), boardTypeEntity);

                // 조회된 내역을 BoardResposeDto로 변환
                List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

                // 조회된 내역 중 5개까지만 보내기
                for (int i = 0; i < (boardList.size() > 5 ? 5 : boardList.size()); i++){
                    boardResponseDtoList.add(new BoardResponseDto(boardList.get(i)));
                }

                // 반환할 List에 담음
                boardResponseAllDtoList.add(new BoardResponseAllDto(boardTypeEntity.getBoardType(), boardTypeEntity.getTypeName(), boardResponseDtoList));
            }
            return boardResponseAllDtoList;
        }

        // 기수, 게시판 타입으로 조회
        List<Board> boardList = boardRepository.findAllByClassIdAndBoardTypeOrderByCreatedAtDesc(user.getClassId(), optionalBoardType.get());

        // BoardTypeResponseDto 리스트로 변환
        List<BoardTypeResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board : boardList) {
            boardResponseDtoList.add(new BoardTypeResponseDto(board, false, 0L, 0L));
        }

        // 반환할 List에 담음
        boardResponseAllDtoList.add(new BoardResponseAllDto(optionalBoardType.get().getBoardType(), optionalBoardType.get().getTypeName(), boardResponseDtoList));
        return boardResponseAllDtoList;
    }

    @Transactional
    public void getDetailBoards(Long boardId, UserDetailsImpl userDetails) {
    }

    @Transactional
    public void createPost(BoardRequestDto postRequestDto, User user) {
        Optional<BoardType> optionalBoardType = boardTypeRepository.queryFindByType(postRequestDto.getBoardType());
        if (optionalBoardType.isEmpty()){
            throw new CustomException(CustomErrorCode.BOARD_TYPE_NOT_FOUND);
        }
        boardRepository.saveAndFlush(new Board(postRequestDto, optionalBoardType.get(), user));
    }

    @Transactional
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
                () -> new CustomException(CustomErrorCode.POST_NOT_FOUND)
        );
    }

    //작성자가 일치 하는지 확인 하는 공통 메서드
    public void isAuthor(Board board, User user) {
        if (!board.getUser().getId().equals(user.getId())) {
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
    }
}
