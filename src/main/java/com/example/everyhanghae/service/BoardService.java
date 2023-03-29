package com.example.everyhanghae.service;

import com.example.everyhanghae.dto.request.BoardRequestDto;
import com.example.everyhanghae.dto.response.*;
import com.example.everyhanghae.entity.*;
import com.example.everyhanghae.exception.CustomErrorCode;
import com.example.everyhanghae.exception.CustomException;
import com.example.everyhanghae.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final LikesRepository likesRepository;
    private final FileUploadRepository fileUploadRepository;
    private final CommentRepository commentRepository;

    private final FileUploadService fileUploadService;

    // 게시글 전체, 유형별 조회
    @Transactional
    public List<BoardResponseAllDto> getTypeBoards(int boardType, int page, User user) {
         /*
         getAllTypeBoards 전체 검색일 때 (default = 0)
         getAllBoards 전체 타입별 검색일 때 (boardType = 1, 2, 3)
         */
        if (boardType == 0) {
            int size = 5;
            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
            Pageable pageable = PageRequest.of(0, size, sort);
            return getAllTypeBoards(user, pageable);
        }

        int size = 12;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return getAllBoards(boardType, user, pageable);
    }

    // 게시글 전체 조회
    public List<BoardResponseAllDto> getAllTypeBoards(User user, Pageable pageable) {
        // 반환할 리스트 선언, 게시글 타입별 리스트를 add 하여 보냄
        List<BoardResponseAllDto> boardResponseAllDtoList = new ArrayList<>();

        // 현재 게시판 타입을 모두 조회
        List<BoardType> boardTypeList = boardTypeRepository.queryFindAll();

        // 타입별로 게시판을 전체 조회
        for (BoardType boardTypeEntity : boardTypeList) {
            // 기수, 게시판 타입으로 조회
            Page<Board> boardList = boardRepository.findAllByClassIdAndBoardType(user.getClassId(), boardTypeEntity, pageable);

            // 조회된 내역을 BoardResposeDto로 변환하기 위한 리스트
            List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

            for (Board board : boardList) {
                boardResponseDtoList.add(new BoardResponseDto(board));
            }

            // 반환할 List에 담음
            boardResponseAllDtoList.add(new BoardResponseAllDto(boardTypeEntity.getBoardType(), boardTypeEntity.getTypeName(), boardResponseDtoList));
        }
        return boardResponseAllDtoList;
    }

    // 게시글 유형별 조회
    public List<BoardResponseAllDto> getAllBoards(int boardType, User user, Pageable pageable) {
        // 반환할 리스트 선언, 게시글 타입별 리스트를 add 하여 보냄
        List<BoardResponseAllDto> boardResponseAllDtoList = new ArrayList<>();

        // 게시판 타입 종류 검색
        Optional<BoardType> optionalBoardType = boardTypeRepository.queryFindByType(boardType);

        if (optionalBoardType.isEmpty()){
            throw new CustomException(CustomErrorCode.CONTENT_NOT_FOUND);
        }

        // 기수, 게시판 타입으로 조회
        Page<Board> boardPage = boardRepository.findAllByClassIdAndBoardType(user.getClassId(), optionalBoardType.get(), pageable);

        if (boardPage.getNumberOfElements() == 0){
            throw new CustomException(CustomErrorCode.CONTENT_NOT_FOUND);
        }

        // BoardTypeResponseDto 리스트로 변환
        List<BoardTypeResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board : boardPage) {
            boolean onLike = onLike(board, user);
            int totalLike = countLikes(board);
            int totalComment = countComments(board);
            boardResponseDtoList.add(new BoardTypeResponseDto(board, onLike, totalLike, totalComment));
        }

        // 반환할 List에 담음
        boardResponseAllDtoList.add(new BoardResponseAllDto(optionalBoardType.get().getBoardType(), optionalBoardType.get().getTypeName(), boardResponseDtoList));

        return boardResponseAllDtoList;
    }

    //공감 갯수
    public int countComments(Board board){
        return commentRepository.countByBoard(board);
    }

    // 게시글 상세조회
    @Transactional
    public BoardDetailResponseDto getDetailBoard(Long boardId, User user) {
        Board board = isExistBoard(boardId);

        boolean onLike = onLike(board, user);
        boolean onMine = board.getUser().getId() == user.getId();
        int totalLike = countLikes(board);
        List<CommentResponseDto> commentResponseList = getCommentResponseList(boardId, user);
        String filePath = null;
        Optional<FileUpload> fileUpload = fileUploadRepository.findByBoard(board);
        if (fileUpload.isPresent()) filePath = fileUploadRepository.findByBoard(board).get().getFilePath();
        return new BoardDetailResponseDto(board, onLike, totalLike, commentResponseList.size(), onMine, filePath, commentResponseList);
    }

    // 댓글 작업 >> 양방향에서 단방향으로 수정 commentrepository에서 직접 불러오기
    public List<CommentResponseDto> getCommentResponseList(Long boardId, User user){
        List<CommentResponseDto> commentResponseList = new ArrayList<>();
        for(Comment comment : commentRepository.findByBoardId(boardId)){
            boolean onMine = comment.getUser().getId() == user.getId();
            commentResponseList.add(new CommentResponseDto(comment, onMine));
        }
        return commentResponseList;
    }
    // comment repository에서 어떤 리스트들이 가져와지는지를 생각을해 사라야....띵킹어바웃

    // 게시글 등록
    @Transactional
    public void createBoard(BoardRequestDto boardRequestDto, User user) {
        Optional<BoardType> optionalBoardType = boardTypeRepository.queryFindByType(boardRequestDto.getBoardType());
        if (optionalBoardType.isEmpty()) {
            throw new CustomException(CustomErrorCode.BOARD_TYPE_NOT_FOUND);
        }

        Board board = boardRepository.save(new Board(boardRequestDto, optionalBoardType.get(), user));

        // 올릴 파일이 없으면 return
        if (boardRequestDto.getFilePath() == null) return;
        if (boardRequestDto.getFilePath() == "") return;

        fileUploadRepository.save(new FileUpload(board, boardRequestDto.getFileName(), boardRequestDto.getFilePath()));
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long boardId, BoardRequestDto boardRequestDto, User user) {
        Board board = isExistBoard(boardId);
        isAuthor(board, user);

//        게시글 작성자 일치여부 확인 > 공통메서드 처리
//        if (!board.getUser().getId().equals(user.getId())) {
//            throw new CustomException(NOT_AUTHOR);
//        }

        // 게시글 수정
        board.update(boardRequestDto.getTitle(), boardRequestDto.getContent());
        boardRepository.save(board);

        Optional<FileUpload> fileUpload = fileUploadRepository.findByBoard(board);
        fileUploadService.deleteFile(fileUpload.get().getFileName());
        fileUploadRepository.deleteAllByBoard(board);

        // 올릴 파일이 없으면 return
        if (boardRequestDto.getFilePath() == null) return;
        if (boardRequestDto.getFilePath() == "") return;

        fileUploadRepository.save(new FileUpload(board, boardRequestDto.getFileName(), boardRequestDto.getFilePath()));
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = isExistBoard(boardId);
        isAuthor(board, user);

        // 연관관계 삭제
        commentRepository.deleteAllByBoard(board);
        likesRepository.deleteAllByBoard(board);
        fileUploadRepository.deleteAllByBoard(board);
        boardRepository.deleteById(boardId);
    }

    // 게시글 공감, 공감 취소
    @Transactional
    public SameBoardResponseDto sameBoard(Long boardId, User user){
        Board board = isExistBoard(boardId);
        Optional<Likes> likes = likesRepository.findByBoardAndUser(board, user);
        boolean like = likes.isEmpty();
        if(like){
            likesRepository.saveAndFlush(new Likes(board, user));
        }
        else{
            likesRepository.deleteById(likes.get().getId());
        }
        return new SameBoardResponseDto(onLike(board, user), countLikes(board));
    }


    // 게시글 존재 하는지 확인 하는 공통 메서드
    public Board isExistBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND)
        );
    }

    // 작성자가 일치 하는지 확인 하는 공통 메서드
    public void isAuthor(Board board, User user) {
        if (!board.getUser().getId().equals(user.getId())) {
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
    }

    // 공감 체크
    public boolean onLike(Board board, User user) {
        if (user == null) return false;
        return likesRepository.existsByBoardAndUser(board, user);
    }

    // 공감 갯수
    public int countLikes(Board board){
        return likesRepository.countByBoard(board);
    }

}
