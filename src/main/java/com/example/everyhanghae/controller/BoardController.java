package com.example.everyhanghae.controller;
import com.example.everyhanghae.dto.request.BoardRequestDto;
import com.example.everyhanghae.exception.ResponseMessage;
import com.example.everyhanghae.security.UserDetailsImpl;
import com.example.everyhanghae.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    // 전체 게시글 조회
    @GetMapping("/boards")
    public ResponseEntity getAllBoards(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("조회 완료" , boardService.getAllBoards(userDetails.getUser()));
    }

    // 게시글 유형별 조회
    @GetMapping("/boards?board-type={boardType}")
    public void getTypeBoards(@RequestParam(value = "boardType", required = false, defaultValue = "1") int boardType, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.getTypeBoards(boardType, userDetails.getUser());
    }

    // 게시글 상세 조회
    @GetMapping("/board/{boardId}")
    public void getDetailBoards(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.getDetailBoards(boardId, userDetails);
    }

    // 게시글 등록
    @PostMapping("board/{boardId}")
    public ResponseEntity createBoard(@PathVariable Long boardId, @RequestBody BoardRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.createPost(postRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("작성 완료", "");
    }

}