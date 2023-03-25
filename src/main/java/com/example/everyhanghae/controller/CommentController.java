package com.example.everyhanghae.controller;


import com.example.everyhanghae.dto.request.CommentRequestDto;
import com.example.everyhanghae.exception.ResponseMessage;
import com.example.everyhanghae.security.UserDetailsImpl;
import com.example.everyhanghae.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.createComment(boardId,commentRequestDto,userDetails.getUser());
        return ResponseMessage.SuccessResponse("댓글 작성 완료", "");
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.update(commentId, commentRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("댓글 수정 완료", "");

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deletePost(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.delete(commentId,userDetails.getUser());
        return ResponseMessage.SuccessResponse("댓글 삭제 완료", "");

    }




}
