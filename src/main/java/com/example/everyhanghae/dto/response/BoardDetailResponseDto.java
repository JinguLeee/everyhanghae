package com.example.everyhanghae.dto.response;

import com.example.everyhanghae.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private int totalLike;
    private int totalComment;
    private boolean onMine;
    private String createdAt;
    private String filePath;
    private List<CommentResponseDto> commentList;

    public BoardDetailResponseDto(Board board, boolean onLike, int totalLike, int totalComment, boolean onMine, String filePath, List<CommentResponseDto> commentResponseDtoList) {
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
        this.filePath = filePath;
        this.commentList = commentResponseDtoList;
    }
}
