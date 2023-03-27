package com.example.everyhanghae.dto.response;

import com.example.everyhanghae.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String userName;
    private String comment;
    private boolean onMine;
    private String createdAt;

    public CommentResponseDto(Comment comment, boolean onMine){
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.userName = (comment.isAnonymous() ? "익명" : comment.getUser().getUsername());
        this.onMine = onMine;
        this.createdAt = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));

    }

}
