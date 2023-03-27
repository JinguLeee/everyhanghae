package com.example.everyhanghae.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ResponseMessage {
    private final String message;
    private final int statusCode;
    private final Object data;

    public static ResponseEntity ErrorResponse(CustomErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ResponseMessage.builder()
                        .message(errorCode.getMessage())
                        .statusCode(errorCode.getHttpStatus().value())
                        .data("")
                        .build()
                );
    }

    // 정규식 globalException을 위해 추가
    public static ResponseEntity ErrorResponse(String message) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseMessage.builder()
                        .message(message)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .data("")
                        .build()
                );
    }

    public static ResponseEntity SuccessResponse(String message, Object data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseMessage.builder()
                        .message(message)
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .build()
                );
    }

}


