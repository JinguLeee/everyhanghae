package com.example.everyhanghae.controller;

import com.example.everyhanghae.dto.request.LoginRequestDto;
import com.example.everyhanghae.dto.request.SignupRequestDto;
import com.example.everyhanghae.exception.CustomErrorCode;
import com.example.everyhanghae.exception.CustomException;
import com.example.everyhanghae.exception.ResponseMessage;
import com.example.everyhanghae.jwt.JwtUtil;
import com.example.everyhanghae.service.UserService;
import com.fasterxml.jackson.core.io.JsonEOFException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //@Valid - 객체 검증을 수행할 때 해당 객체가 어떤 제약 조건을 만족하는지 검증한다.
    //해당 객체가 유효하지 않으면 BindingResult 객체에 에러 정보를 담는다.
    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(CustomErrorCode.NOT_PROPER_INPUTFORM);
        }
        return ResponseMessage.SuccessResponse(userService.signup(signupRequestDto) , "");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginRequestDto user = userService.login(loginRequestDto);
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getLoginId()));
        return ResponseMessage.SuccessResponse("로그인 성공!", "");
    }

}
