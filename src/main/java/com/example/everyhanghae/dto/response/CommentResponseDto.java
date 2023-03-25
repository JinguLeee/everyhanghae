package com.example.everyhanghae.dto.response;

import com.example.everyhanghae.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}


//    public BoardTypeResponseDto(Board board, boolean onLike, Long totalLike, Long totalComment) {
//        this.id = board.getId();
//        this.title = board.getTitle();
//        this.onLike = onLike;
//        this.totalLike = totalLike;
//        this.totalComment = totalComment;
//        this.createdAt = board.getCreatedAt().format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
//    }