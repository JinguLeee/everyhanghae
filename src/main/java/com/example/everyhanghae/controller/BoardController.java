package com.example.everyhanghae.controller;
import com.example.everyhanghae.security.UserDetailsImpl;
import com.example.everyhanghae.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    // 전체 게시글 조회
    @GetMapping("/boards")
    public void getAllBoards(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.getAllBoards(userDetails.getUser());
    }

    // 게시글 유형별 조회
    @GetMapping("/boards?board-type={boardType}")
    public void getTypeBoards(@RequestParam(value = "boardType", required = false, defaultValue = "1") int boardType, @AuthenticationPrincipal UserDetailsImpl userDetails) {

    }

    // 게시글 상세 조회
    @GetMapping("/board")
    public void getDetailBoards(){

    }

}
