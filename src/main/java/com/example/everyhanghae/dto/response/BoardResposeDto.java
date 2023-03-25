package com.example.everyhanghae.dto.response;
import com.example.everyhanghae.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResposeDto {
    private String title;
    private String content;

    public BoardResposeDto(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }
}
