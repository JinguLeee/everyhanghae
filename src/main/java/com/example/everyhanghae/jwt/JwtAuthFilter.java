package com.example.everyhanghae.jwt;

import com.example.everyhanghae.dto.response.SecurityExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    /** OncePerRequestFilter와 JwtAuthFilter
     * 모든 요청에 대해 한번만 실행되도록 보장하는 필터
     * JwtAuthFilter는 HTTP 요청에 대해 JWT 인증을 수행
     * JwtAuthFilter는 모든 HTTP 요청을 가로채어 JWT 인증을 수행하며, 인증이 완료된 요청에 대해서만 다음 단계로 진행할 수 있도록 보장
     */

    private final JwtUtil jwtUtil;

    @Override //abstract 로 정의된 추상클래스의 추상메서드를 오버라이딩함 (OncePerRequestFilter)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /** protected void doFilterInternal 코드 분석
         * doFilterInternal 메서드 : Servlet 필터에서 가장 핵심적인 부분, HTTP 요청을 처리하는 코드가 구현
         * 이 오버라이딩한 메서드는 JWT(Json Web Token) 인증을 처리하는 코드를 구현하고 있음
         * HttpServletRequest request :  클라이언트의 HTTP 요청 정보
         * HttpServletResponse response : 서버가 클라이언트로 보낼 HTTP 응답 정보
         * FilterChain filterChain : FilterChain은 필터 체인의 다음 필터를 호출하는 역할을 수행
         */

        String token = jwtUtil.resolveToken(request);
        //클라이언트가 요청한 정보를(request) token에 담음

        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
                /** !jwtUtil.validateToken(token)값이 false라면 jwtExceptionHandler호출하여 실행
                 * 앞에 참조하는 변수도 없으므로 같은 클래스 안에 정의된 함수가 있음
                 */
            }
            Claims info = jwtUtil.getUserInfoFromToken(token);
            //토큰을 가져와서 info에 넣음
            setAuthentication(info.getSubject());
            //토큰의 subject(loginId)를 가져와서 파라미터로 넣음
        }
        filterChain.doFilter(request,response);
    }

    //하지만 사용하고 있지 않은




    public void setAuthentication(String loginId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(loginId); // 인증 객체를 만든다
        context.setAuthentication(authentication);
        //Authentication 객체를 생성하고, 이를 SecurityContext 객체에 저장
        SecurityContextHolder.setContext(context);
        //SecurityContextHolder에 SecurityContext 객체를 저장하는 작업
    }

    /**
     *
     * @SecurityContext :클래스는 스프링 시큐리티에서 인증 정보와 권한 정보 등의 보안 관련 정보를 저장하는 객체
     * @SecurityContextHolder :  스프링 시큐리티에서 인증 정보와 권한 정보 등의 보안 관련 정보를 저장하는 정적 변수
     * @Authentication : 스프링 시큐리티에서 인증 정보를 나타내는 객체. 인증 정보에는 사용자의 아이디, 비밀번호, 권한 등이 포함
     *                  1) Principal : 증된 사용자를 나타내는 객체로, 사용자의 아이디나 이메일 주소 등의 정보가 포함
     *                  2) Credential : 인증에 사용된 자격 증명 정보를 나타내는 객체로, 비밀번호나 암호화된 인증 토큰 등이 포함
     * @.createEmptyContext() : SecurityContextHolder.createEmptyContext() 메서드는 빈 SecurityContext 객체를 생성하는 메서드
     *                          이후 Authentication 객체를 생성하고, 이를 SecurityContext 객체에 저장한 후,
     *                          SecurityContextHolder에 SecurityContext 객체를 저장하는 등의 작업을 수행
     * @.setAuthentication() : SecurityContext 객체에 Authentication 객체를 설정하는 메서드
     * @.setContext() : SecurityContextHolder에 SecurityContext 객체를 설정하는 메서드
     *                  setContext() 메서드는 주어진 SecurityContext 객체를 SecurityContextHolder에 설정
     *                  이를 통해, 현재 실행 중인 스레드에서 SecurityContext 객체를 참조할 수 있도록 지원
     */

    //에러 메세지를 담아 반환할 객체를 만드는 메서드
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        //response에 반환코드와 반환 콘텐츠 타입을 설정함.
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            //new ObjectMapper() 매서드를 호출하기 위해 만든 인스턴스로 아무 내용도 저장되지 않은 상태(기본 설정값으로 초기화된 상태)
            response.getWriter().write(json);
            //json 변수에 저장된 JSON 문자열을 출력 스트림에 출력하고, 이를 HTTP 응답(Response)의 body에 쓰는 것
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}

/** 메서드 설명
 * @.setStatus() : HttpServletResponse 인터페이스에서 제공하는 메서드 중 하나로, 클라이언트에 반환될 HTTP 응답 코드를 설정하는 메서드
 *                객체를 새로 만들어 저장하지 않는 이유는 response에 담기 때문이다.
 * @.setContentType() : HttpServletResponse 인터페이스에서 제공하는 메서드 중 하나로, 클라이언트에게 반환될 HTTP 응답의 콘텐츠 타입(Content-Type)을 설정하는 메서드
 *                      .setContentType("application/json")은 json형식의 데이터를 반환하는 것을 의미
 * @.writeValueAsString() :  Jackson 라이브러리에서 제공하는 ObjectMapper 클래스의 메서드 중 하나로, 자바 객체를 JSON 형식의 문자열로 변환하는 메서드
 * @.getWriter() : 메서드는 HTTP 응답(Response)의 body에 데이터를 출력할 수 있는 PrintWriter 객체를 반환
 * @.write() : PrintWriter 객체가 참조하는 출력 스트림에 문자열을 출력하는 메서드
 */

// TODO : Jackson 라이브러리는 무엇인가?
/**
 * 자바 객체를 JSON 형식으로 변환하거나, JSON 형식의 데이터를 자바 객체로 변환하는 라이브러리
 * ObjectMapper 클래스를 사용하여 자바 객체와 JSON 데이터를 변환하며, 다양한 설정 옵션을 제공하여 변환 방식을 변경할 수 있음
 */

// TODO : [중요] 상수 풀(Constant Pool) 문자열 리터럴을 저장하는 장소
/** String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
 * new ObjectMapper().writeValueAsString : 여기서 new ObjectMapper()는 .writeValueAsString함수를 호출하기 위해 만들어진 객체
 * new SecurityExceptionDto(statusCode, msg) 힙영역에 있는 메모리가 .writeValueAsString로 인해 자바객체가 json형식의 문자열로 변환
 * 이렇게 변환된 문자열 리터럴은 상수풀(메모리)에 저장됨
 * 상수풀에 저장된 주소값을 json이 참조하게 됨
 */

// TODO : [중요] 상수 풀(Constant Pool)은 누가 관리하는가? JVM
/**
 * 상수 풀(Constant Pool)은 JVM이 관리합니다. JVM은 자바 프로그램이 실행될 때, 상수 풀을 생성하고, 자바 클래스 파일에서 참조하는 상수들을 상수 풀에 저장
 * 상수 풀은 클래스마다 하나씩 생성되며, 클래스의 상수들은 해당 클래스의 상수 풀에 저장
 * 문자열 리터럴과 같은 상수들은 모든 클래스에서 참조할 수 있으므로, JVM은 모든 클래스의 상수 풀에서 이러한 상수들을 공유
 */
