package com.example.everyhanghae.service;

import com.example.everyhanghae.dto.request.SignupRequestDto;
import com.example.everyhanghae.entity.ClassCode;
import com.example.everyhanghae.entity.User;
import com.example.everyhanghae.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ClassCode classCode;

    @Nested
    @DisplayName("회원 등록")
    class addUser{
        private String loginId;
        private String userName;
        private String password;
        private String email;
        private String secretKey;

        @BeforeEach
        void setup(){
            loginId = "jingu";
            userName = "진구";
            password = "jingu123";
            email = "jingu@naver.com";
            secretKey = "ABCDE";
        }

        @Test
        @DisplayName("정상 케이스")
        void addUser_Normal() {
            int classId = 13;

            SignupRequestDto signupRequestDto = new SignupRequestDto(loginId, userName, password, email, secretKey);


            when(classCode.getClassId())
                    .thenReturn(classId);

            User user = new User(signupRequestDto, password, classCode.getClassId());
            assertDoesNotThrow( () -> {
                userRepository.save(user);
            });
        }
    }
}