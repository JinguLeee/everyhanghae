package com.example.everyhanghae.dto.response;

import lombok.Getter;

@Getter
public class SameBoardResponseDto {
    private boolean onLike;
    private int totalCount;

    public SameBoardResponseDto(boolean onLike, int totalCount){
        this.onLike = onLike;
        this.totalCount = totalCount;
    }

}
