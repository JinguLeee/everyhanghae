package com.example.everyhanghae.dto.response;

import com.example.everyhanghae.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class BoardTypeResponseDto {

    private Long id;
    private String title;
    private boolean onLike;
    private int totalLike;
    private int totalComment;
    private String createdAt;

    public BoardTypeResponseDto(Board board, boolean onLike, int totalLike, int totalComment) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.onLike = onLike;
        this.totalLike = totalLike;
        this.totalComment = totalComment;
        this.createdAt = board.getCreatedAt().format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
    }

}
