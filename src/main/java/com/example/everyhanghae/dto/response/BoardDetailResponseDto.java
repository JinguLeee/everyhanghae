package com.example.everyhanghae.dto.response;

import com.example.everyhanghae.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class BoardDetailResponseDto {

    private Long id;
    private int boardType;
    private String typeName;
    private String title;
    private String content;
    private String userName;
    private boolean onLike;
    private Long totalLike;
    private Long totalComment;
    private boolean onMine;
    private String createdAt;

    public BoardDetailResponseDto(Board board, boolean onLike, Long totalLike, Long totalComment, boolean onMine) {
        this.id = board.getId();
        this.boardType = board.getBoardType().getBoardType();
        this.typeName = board.getBoardType().getTypeName();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userName = board.getUser().getUsername();
        this.onLike = onLike;
        this.totalLike = totalLike;
        this.totalComment = totalComment;
        this.onMine = onMine;
        this.createdAt = board.getCreatedAt().format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
    }
}
