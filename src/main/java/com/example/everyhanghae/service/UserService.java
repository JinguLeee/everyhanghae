package com.example.everyhanghae.service;

import com.example.everyhanghae.dto.request.LoginRequestDto;
import com.example.everyhanghae.dto.request.SignupRequestDto;
import com.example.everyhanghae.entity.ClassCode;
import com.example.everyhanghae.entity.User;
import com.example.everyhanghae.exception.CustomErrorCode;
import com.example.everyhanghae.exception.CustomException;
import com.example.everyhanghae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    @Transactional
    public String signup(SignupRequestDto signupRequestDto){
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
        Optional<ClassCode> classCode = userRepository.queryFindBySecret(signupRequestDto.getSecretKey());
        if (classCode.isEmpty()){
            throw new CustomException(CustomErrorCode.SECRET_KEY_NOT_FOUND);
        }
        User user = new User(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()), classCode.get().getClassId());
        userRepository.save(user);

        return "회원가입 성공";
    }

    @Transactional
    public LoginRequestDto login(LoginRequestDto loginRequestDto){
        //사용자 확인
        User user = userRepository.findByLoginId(loginRequestDto.getLoginId()).orElseThrow(
                () -> new CustomException(CustomErrorCode.USER_NOT_FOUND)
        );
        // 비밀번호 확인
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            throw  new CustomException(CustomErrorCode.NOT_PROPER_PASSWORD);
        }
        return new LoginRequestDto(user.getLoginId(), user.getPassword());
    }
}
