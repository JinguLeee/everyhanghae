package com.example.everyhanghae.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String loginId;

    private String password;

    public LoginRequestDto(String loginid, String password){
        this.loginId = loginid;
        this.password = password;
    }

}
