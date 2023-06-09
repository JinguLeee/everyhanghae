package com.example.everyhanghae.controller;

import com.example.everyhanghae.dto.request.LoginRequestDto;
import com.example.everyhanghae.dto.request.SignupRequestDto;
import com.example.everyhanghae.exception.ResponseMessage;
import com.example.everyhanghae.security.UserDetailsImpl;
import com.example.everyhanghae.service.KakaoService;
import com.example.everyhanghae.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController         //해당 클래스가 RESTful API를 처리하는 컨트롤러임을 나타내며, 이 클래스에서 반환되는 값들은 모두 HTTP Response Body에 포함
@RequiredArgsConstructor    //생성자를 자동으로 생성
@RequestMapping("/api/users")   //요청 경로에 따라 요청을 처리하는 메서드
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;

    //@Valid - 객체 검증을 수행할 때 해당 객체가 어떤 제약 조건을 만족하는지 검증한다.
    //해당 객체가 유효하지 않으면 BindingResult 객체에 에러 정보를 담는다.
    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        //@Valid - 해당 파라미터가 유효성 검사를 통과해야만 메소드가 실행될 수 있도록 하는 기능
        //@RequestBody - HTTP 요청의 본문에 있는 데이터를 SignupRequestDto 객체로 매핑
        //bindingResult - 유효성 검사 결과를 저장하는 객체

        userService.signup(signupRequestDto);
        return ResponseMessage.SuccessResponse("회원가입 성공" , null);
        //회원가입 성공 결과를 ResponseMessage 객체에 담아 반환
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return ResponseMessage.SuccessResponse("로그인 성공!", null);
    }

    @DeleteMapping()
    public ResponseEntity deleteUsers(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        userService.deleteUsers(userDetails.getUser(), response);
        return ResponseMessage.SuccessResponse("회원탈퇴 완료", null);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        kakaoService.kakaoLogin(code, response);
        return ResponseMessage.SuccessResponse("카카오 로그인 완료", null);
    }
}
