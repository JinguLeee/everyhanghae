package com.example.everyhanghae.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {

    private String title;
    private String content;
    private int boardType;
    private String fileName;
    private String filePath;

}