package com.example.everyhanghae.entity;

import com.example.everyhanghae.dto.request.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private long class_id;


    public User(SignupRequestDto signupRequestDto, String password) {
        this.loginId = signupRequestDto.getLoginId();
        this.username = signupRequestDto.getUserName();
        this.password = password;
    }
}
