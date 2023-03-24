package com.example.everyhanghae.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SignupRequestDto {

    //영어 소문자, 숫자만 입력 가능. 4글자 이상 20글자 미만으로 입력해야 함
    @NotNull
//    @Pattern(regexp = "^[a-z0-9]{4,20}$")
    private String loginId;

    //영어 소문자, 숫자만 입력 가능. 1글자 이상 12글자 미만으로 입력해야 함
    @NotNull
//    @Pattern(regexp = "^[a-z0-9]{1,12}$")
    private String userName;

    //영어 대소문자, 숫자, 특수기호만 입력 가능. 8글자 이상 20글자 미만으로 입력해야 함
    @NotNull
//    @Pattern(regexp = "^[a-zA-Z0-9`~!@#$%^&*()-_=+](?=.*[@$!%*#?&].*[@$!%*#?&]){8,20}$")
    private String password;

    //@, 영어 소문자, 숫자만 입력 가능. 3글자 이상 30글자 미만으로 입력해야 함
    @NotNull
//    @Pattern(regexp = "^(?=.*[@])[a-z0-9]{3,30}$")
    private String email;

    @NotNull
    private String secretKey;

    //한글만 입력 가능. 1글자 이상 10글자 미만으로 입력해야 함
    //(regexp = "^[가-힣]{1,10}$")

}
