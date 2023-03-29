package com.example.everyhanghae.dto.response;

import lombok.Getter;

@Getter
public class FileUploadResponseDto {
    private String fileName;
    private String filePath;

    public FileUploadResponseDto(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

}
