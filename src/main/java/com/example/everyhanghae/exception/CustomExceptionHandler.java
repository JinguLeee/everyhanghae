package com.example.everyhanghae.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ResponseMessage> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ResponseMessage.ErrorResponse(e.getErrorCode());

    }

    // 정규식 오류 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleNotValidException(MethodArgumentNotValidException e){
        log.error("handleNotValidException throw MethodArgumentNotValidException : {}", HttpStatus.BAD_REQUEST.value());
        String message = e.getFieldError().getDefaultMessage().split(":")[0];
        return ResponseMessage.ErrorResponse(message);
    }

}

