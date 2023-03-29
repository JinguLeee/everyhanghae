package com.example.everyhanghae.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SignupRequestDto {

    //영어 소문자, 숫자만 입력 가능. 4글자 이상 20글자 미만으로 입력해야 함
    @NotNull
    @Pattern(regexp = "^*[a-z0-9]{4,20}$", message = "영문 소문자, 숫자, 4~20자")
    private String loginId;

    //영어 소문자, 숫자만 입력 가능. 1글자 이상 12글자 미만으로 입력해야 함
    @NotNull
    private String userName;

    //영어 대소문자, 숫자, 특수기호만 입력 가능. 8글자 이상 20글자 미만으로 입력해야 함
    @NotNull
    @Pattern(regexp = "^(?=.*?[a-zA-Z])(?=.*?\\d|\\W).{8,20}$", message = "영문, 숫자, 특문이 2종류 이상 조합된 8-20자")
    private String password;

    //@, 영어 소문자, 숫자만 입력 가능. 3글자 이상 30글자 미만으로 입력해야 함
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$", message = "올바른 이메일을 입력하세요")
    private String email;

    @NotNull
    private String secretKey;

    public SignupRequestDto(String loginId, String userName, String password, String email, String secretKey) {
        this.loginId = loginId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.secretKey = secretKey;
    }

    //한글만 입력 가능. 1글자 이상 10글자 미만으로 입력해야 함
    //(regexp = "^[가-힣]{1,10}$")

}
