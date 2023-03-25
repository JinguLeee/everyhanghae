package com.example.everyhanghae.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseAllDto<T> {

    private int boardType;
    private String typeName;
    private List<T> boardContent;

    public BoardResponseAllDto(int boardType, String typeName, List<T> boardContent) {
        this.boardType = boardType;
        this.typeName = typeName;
        this.boardContent = boardContent;
    }

}