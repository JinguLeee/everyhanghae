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

    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int classId;

    @Column(nullable = false)
    private int email;

    public User(SignupRequestDto signupRequestDto, String password, int classId) {
        this.loginId = signupRequestDto.getLoginId();
        this.username = signupRequestDto.getUserName();
        this.password = password;
        this.classId = classId;
    }

    public User(Long kakaoId, String userName, String password) {
        this.kakaoId = kakaoId;
        this.username = userName;
        this.password = password;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

}
