package com.example.everyhanghae.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    DUPLICATE_USER(BAD_REQUEST, "중복된 아이디가 존재합니다"),
    DUPLICATE_NICKNAME(BAD_REQUEST, "중복된 닉네임이 존재합니다"),
    NOT_PROPER_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_LENGTH(BAD_REQUEST, "입력한 문자열 길이가 맞지 않습니다."),
    NOT_PROPER_URLFORM(BAD_REQUEST, "입력한 URL 형식이 맞지 않습니다."),
    NOT_AUTHOR(BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    NOT_INPUT(BAD_REQUEST, "입력한 문자열 조건이 맞지 않습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "등록된 사용자가 없습니다"),
    BOARD_NOT_FOUND(NOT_FOUND, "선택한 게시물을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "선택한 댓글을 찾을 수 없습니다."),
    SECRET_KEY_NOT_FOUND(NOT_FOUND, "해당 시크릿 키는 존재하지 않습니다."),
    BOARD_TYPE_NOT_FOUND(NOT_FOUND, "게시글 유형을 찾을 수 없습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;

}

