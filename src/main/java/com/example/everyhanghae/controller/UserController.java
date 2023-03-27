package com.example.everyhanghae.controller;

import com.example.everyhanghae.dto.request.LoginRequestDto;
import com.example.everyhanghae.dto.request.SignupRequestDto;
import com.example.everyhanghae.exception.ResponseMessage;
import com.example.everyhanghae.jwt.JwtUtil;
import com.example.everyhanghae.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController         //해당 클래스가 RESTful API를 처리하는 컨트롤러임을 나타내며, 이 클래스에서 반환되는 값들은 모두 HTTP Response Body에 포함
@RequiredArgsConstructor    //생성자를 자동으로 생성
@RequestMapping("/api/users")   //요청 경로에 따라 요청을 처리하는 메서드
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //@Valid - 객체 검증을 수행할 때 해당 객체가 어떤 제약 조건을 만족하는지 검증한다.
    //해당 객체가 유효하지 않으면 BindingResult 객체에 에러 정보를 담는다.
    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        //@Valid - 해당 파라미터가 유효성 검사를 통과해야만 메소드가 실행될 수 있도록 하는 기능
        //@RequestBody - HTTP 요청의 본문에 있는 데이터를 SignupRequestDto 객체로 매핑
        //bindingResult - 유효성 검사 결과를 저장하는 객체

//        if (bindingResult.hasErrors()) {
//            throw new CustomException(CustomErrorCode.NOT_PROPER_INPUTFORM);
//            //bindingResult 객체에 에러 정보가 저장되어 있다면, CustomException을 발생시키는 예외 처리를 수행
//        }
        return ResponseMessage.SuccessResponse(userService.signup(signupRequestDto) , "");
        //회원가입 성공 결과를 ResponseMessage 객체에 담아 반환
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginRequestDto user = userService.login(loginRequestDto);
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getLoginId()));
        return ResponseMessage.SuccessResponse("로그인 성공!", "");
    }

}
