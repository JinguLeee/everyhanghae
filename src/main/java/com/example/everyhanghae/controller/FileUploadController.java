package com.example.everyhanghae.controller;

import com.example.everyhanghae.exception.ResponseMessage;
import com.example.everyhanghae.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class FileUploadController {

    private final FileUploadService fileUploadService;
    @PostMapping("/file")
    public ResponseEntity uploadFile(@RequestPart List<MultipartFile> multipartFile) {
        return ResponseMessage.SuccessResponse("파일 등록 완료" , fileUploadService.uploadFile(multipartFile));
    }

    @DeleteMapping("/file")
    public ResponseEntity deleteFile(@RequestParam(value = "file-name") String fileName) {
        if (fileName.equals("")) return ResponseMessage.SuccessResponse("파일 없음", null);
        fileUploadService.deleteFile(fileName);
        return ResponseMessage.SuccessResponse("파일 삭제 완료", null);
    }

}