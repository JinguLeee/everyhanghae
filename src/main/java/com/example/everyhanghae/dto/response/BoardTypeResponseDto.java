package com.example.everyhanghae.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardTypeResponseDto {

    private int boardType;
    private List<BoardResposeDto> boardResposeDtoList;

    public BoardTypeResponseDto(int boardType, List<BoardResposeDto> boardResposeDtoList) {
        this.boardType = boardType;
        this.boardResposeDtoList = boardResposeDtoList;
    }

}