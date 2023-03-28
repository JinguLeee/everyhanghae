package com.example.everyhanghae.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String loginId;
    private String password;

    public LoginRequestDto(String loginId, String password){
        this.loginId = loginId;
        this.password = password;
    }

}
