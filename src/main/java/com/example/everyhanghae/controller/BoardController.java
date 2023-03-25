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

    // 게시글 상세 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity getDetailBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("게시글 상세 조회 완료" , boardService.getDetailBoard(boardId, userDetails.getUser()));
    }

    // 게시글 등록
    @PostMapping("board")
    public ResponseEntity createBoard(@RequestBody BoardRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.createPost(postRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("작성 완료", "");
    }

    @PatchMapping("board/{boardId}")
    public ResponseEntity updatePost(@PathVariable Long boardId, @RequestBody BoardRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.updatePost(boardId, postRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("수정 완료", "");
    }

    @DeleteMapping("board/{boardId}")
    public ResponseEntity<String> deletePost(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.deletePost(boardId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body("게시글 삭제 성공");
    }

}