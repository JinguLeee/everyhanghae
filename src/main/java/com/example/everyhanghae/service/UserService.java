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
        //아이디 형태 확인. 영어 소문자, 숫자만 입력 가능. 4글자 이상 20글자 미만으로 입력해야 함
        String loginId = signupRequestDto.getLoginId();
//        if(loginId == null || loginId.length() < 4|| loginId.length() >= 20){
//            //빈 문자열이거나, 문자열이 4미만, 20초과할 경우 에러메세지를 보낸다.
//            throw new CustomException(CustomErrorCode.NOT_LENGTH);
//        }
//
//        for(int i = 0; i< loginId.length(); i++){
//            char c = loginId.charAt(i);
//            if(!Character.isLowerCase(c) && !Character.isDigit(c)){//Character - 문자를 다루는 기능을 제공
//                //소문자가 아니거나 숫자가 아니면 에러메세지를 보낸다.
//                throw new CustomException(CustomErrorCode.NOT_INPUT);
//            }
//        }
//
        // 회원아이디 중복 확인
        boolean found = userRepository.findByLoginId(signupRequestDto.getLoginId()).isPresent();
        System.out.println("found = " + found);
        if (found) {
            throw new CustomException(CustomErrorCode.DUPLICATE_USER);
        }
//
        // commnetrepository.findby한걸 생성자에 넣어주면 끝이다. 조인을해서 쿼리로 가져오던가.
        //
//        //닉네임 형태 확인. 영어 소문자, 숫자만 입력 가능. 1글자 이상 12글자 미만으로 입력해야 함
//        String userName = signupRequestDto.getUserName();
//        if(userName == null || userName.length() < 1|| userName.length() >= 12){
//            //빈 문자열이거나, 문자열이 1미만, 12이상일 경우 에러메세지를 보낸다.
//            throw new CustomException(CustomErrorCode.NOT_LENGTH);
//        }
//
//        for(int i = 0; i< userName.length(); i++){
//            char c = userName.charAt(i);
//            if(!Character.isLowerCase(c) && !Character.isDigit(c)){//Character - 문자를 다루는 기능을 제공
//                //소문자가 아니거나 숫자가 아니면 에러메세지를 보낸다.
//                throw new CustomException(CustomErrorCode.NOT_INPUT);
//            }
//        }
//
        // 닉네임 중복 확인
        found = userRepository.findByUsername(signupRequestDto.getUserName()).isPresent();
        if (found) {
            throw new CustomException(CustomErrorCode.DUPLICATE_NICKNAME);
        }
//
//        //비밀번호 형태 확인. 영어 대소문자, 숫자, 특수기호만 입력 가능.특수기호 2개 이상, 8글자 이상 20글자 미만으로 입력해야 함
//        String password = signupRequestDto.getPassword();
//        if(password == null || password.length() < 8|| password.length() >= 20){
//            //빈 문자열이거나, 문자열이 8미만, 20이상일 경우 에러메세지를 보낸다.
//            throw new CustomException(CustomErrorCode.NOT_LENGTH);
//        }
//
//        for(int i = 0; i< password.length(); i++){
//            char c = password.charAt(i);
//            if(!Character.isLowerCase(c) && !Character.isDigit(c)){//Character - 문자를 다루는 기능을 제공
//                //소문자가 아니거나 숫자가 아니면 에러메세지를 보낸다.
//                throw new CustomException(CustomErrorCode.NOT_INPUT);
//            }
//        }
//        //이메일 형태 확인. @, 영어 소문자, 숫자만 입력 가능. 3글자 이상 30글자 미만으로 입력해야 함
//        String email = signupRequestDto.getEmail();
//        if(email == null || email.length() < 3|| email.length() >= 30){
//            //빈 문자열이거나, 문자열이 8미만, 20이상일 경우 에러메세지를 보낸다.
//            throw new CustomException(CustomErrorCode.NOT_LENGTH);
//        }
//        if(!email.contains("@")){
//            //!email.contains("@") - email문자열 안에 '@'가 없을경우 조건식이 참이된다.
//            throw new CustomException(CustomErrorCode.NOT_EMAIL);
//        }
//
//        for(int i = 0; i< email.length(); i++){
//            char c = email.charAt(i);
//            if(!Character.isLowerCase(c) && !Character.isDigit(c)){//Character - 문자를 다루는 기능을 제공
//                //소문자가 아니거나 숫자가 아니면 에러메세지를 보낸다.
//                throw new CustomException(CustomErrorCode.NOT_INPUT);
//            }
//        }
//
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
