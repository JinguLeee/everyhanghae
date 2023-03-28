package com.example.everyhanghae.service;

import com.example.everyhanghae.dto.request.LoginRequestDto;
import com.example.everyhanghae.dto.request.SignupRequestDto;
import com.example.everyhanghae.entity.ClassCode;
import com.example.everyhanghae.entity.User;
import com.example.everyhanghae.exception.CustomErrorCode;
import com.example.everyhanghae.exception.CustomException;
import com.example.everyhanghae.jwt.JwtUtil;
import com.example.everyhanghae.repository.ClassCodeRepository;
import com.example.everyhanghae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ClassCodeRepository classCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto){
        // 회원아이디 중복 확인
        boolean found = userRepository.findByLoginId(signupRequestDto.getLoginId()).isPresent();
        if (found) {
            throw new CustomException(CustomErrorCode.DUPLICATE_USER);
        }

        // 닉네임 중복 확인
        found = userRepository.findByUsername(signupRequestDto.getUserName()).isPresent();
        if (found) {
            throw new CustomException(CustomErrorCode.DUPLICATE_NICKNAME);
        }

        // 시크릿코드 찾기
        Optional<ClassCode> classCode = classCodeRepository.queryFindBySecret(signupRequestDto.getSecretKey());
        if (classCode.isEmpty()){
            throw new CustomException(CustomErrorCode.SECRET_KEY_NOT_FOUND);
        }

        // 회원 저장 (비밀번호 인코딩한 값과 기수 코드를 함께 저장)
        User user = new User(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()), classCode.get().getClassId());
        userRepository.save(user);
    }

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        // 사용자 확인
        User user = userRepository.findByLoginId(loginRequestDto.getLoginId()).orElseThrow(
                () -> new CustomException(CustomErrorCode.USER_NOT_FOUND)
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            throw  new CustomException(CustomErrorCode.NOT_PROPER_PASSWORD);
        }

        // 사용자의 기수를 검색해서 토큰에 같이 넣어줌
        Optional<ClassCode> classCode = classCodeRepository.findByClassId(user.getClassId());
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getLoginId(), classCode.get().getClassName()));
    }
}
