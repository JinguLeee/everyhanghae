package com.example.everyhanghae.dto.response;

import lombok.Getter;

@Getter
public class SamePostResponseDto {
    private boolean onLike;
    private long totalCount;

    public SamePostResponseDto(boolean onLike, long totalCount){
        this.onLike = onLike;
        this.totalCount = totalCount;
    }
}
