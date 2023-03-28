package com.example.everyhanghae.controller;
import com.example.everyhanghae.dto.request.BoardRequestDto;
import com.example.everyhanghae.exception.ResponseMessage;
import com.example.everyhanghae.security.UserDetailsImpl;
import com.example.everyhanghae.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체, 유형별 조회
    @GetMapping("/boards")
    public ResponseEntity getTypeBoards(@RequestParam(value = "board-type", required = false, defaultValue = "0") int boardType, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("게시글 조회 완료" , boardService.getTypeBoards(boardType, userDetails.getUser()));
    }

    // 페이징 처리 테스트
    @GetMapping("/boards/test")
    public ResponseEntity getTypeBoardsTest(@RequestParam(value = "board-type", defaultValue = "0") int boardType, @RequestParam(defaultValue = "1") int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("게시글 조회 완료" , boardService.getTypeBoardsTest(boardType, page-1, userDetails.getUser()));
    }

    // 게시글 상세 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity getDetailBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("게시글 상세 조회 완료" , boardService.getDetailBoard(boardId, userDetails.getUser()));
    }

    // 게시글 등록
    @PostMapping("board")
    public ResponseEntity createBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.createBoard(boardRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("작성 완료", null);
    }

    @PatchMapping("board/{boardId}")
    public ResponseEntity updateBoard(@PathVariable Long boardId, @RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.updateBoard(boardId, boardRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("수정 완료", null);
    }

    @DeleteMapping("board/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.deleteBoard(boardId, userDetails.getUser());
        return ResponseMessage.SuccessResponse("게시글 삭제 성공", null);
    }

    //게시글 공감
    @PostMapping("boards/{boardId}")
    public ResponseEntity sameBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("게시글 공감 완료", boardService.sameBoard(boardId, userDetails.getUser()));
    }

}