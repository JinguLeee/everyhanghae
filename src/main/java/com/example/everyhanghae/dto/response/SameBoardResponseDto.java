package com.example.everyhanghae.dto.response;

import lombok.Getter;

@Getter
public class SameBoardResponseDto {
    private boolean onLike;
    private long totalCount;

    public SameBoardResponseDto(boolean onLike, long totalCount){
        this.onLike = onLike;
        this.totalCount = totalCount;
    }
}
