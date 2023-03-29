package com.example.everyhanghae.config;

import com.example.everyhanghae.jwt.JwtAuthFilter;
import com.example.everyhanghae.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * configuration = Spring Security를 설정하기 위한 기능
 * @Configuration : spring Configuration 클래스임을 나타냄
 * @RequiredArgsConstructor : 클래스 필드에 대한 생성자를 자동으로 생성. JwtUtil클래스의 객체를 생성자로 주입받아 사용
 * @EnableWebSecurity : Spring Security를 활성화하도록 지시
 * @EnableGloblaMethodSecurity : 스프링 서큐리티에서 메소드 수준의 보안을 지원하도록 설정. @Secured 어노테이션을 사용하여 메소드에 대한 엑세스 권한을 제어할 수 있게 함.
 *
 */
@Configuration //@Bean 어노테이션을 사용하여 해당 메소드가 빈으로 등록될 수 있도록 설정가능한 클래스
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {
    // TODO : configration class에서 하는 역할
    /**
     * Java-based Configuration 방식을 사용하여 애플리케이션의 설정 정보를 정의.
     * 이 클래스에서는 애플리케이션에 필요한 Bean들을 정의하고, 의존성을 주입하여 컴포넌트 간의 연결을 설정
     * @1)bean정의 : @Bean 어노테이션을 사용하여 Bean을 정의합니다. Bean은 Spring이 관리하는 객체로서, 컨테이너에서 생성되어 주입
     * @2)의존성주입 :@Autowired 어노테이션을 사용하여 의존성을 주입합니다. 주입할 Bean을 탐색하여 해당 클래스의 인스턴스에 주입
     * @3)프로퍼티설정: @Value 어노테이션을 사용하여 프로퍼티 값을 설정.애플리케이션에서 사용하는 설정 값을 외부에서 주입받아 사용가능.
     * @4)조건부구성: @Conditional 어노테이션을 사용하여 특정 조건에 따라 구성 정보를 변경가능
     * @5)프록시생성: @EnableAspectJAutoProxy 어노테이션을 사용하여 AOP 프록시를 생성. AOP 프록시는 클래스의 메소드 호출을 가로채어 추가적인 작업을 수행
     * @6)메소드실행순서지정: @DependsOn 어노테이션을 사용하여 Bean 생성 순서를 지정가능
     * @Configuration 어노테이션이 사용된 클래스에서는 이러한 작업을 통해 Spring 애플리케이션의 설정 정보를 정의하고, Bean을 등록하여 컨테이너에서 사용할 수 있도록함
     */

    private final JwtUtil jwtUtil; //JwtUtl을 DI 받음 (주입)
    // TODO : spring security에서 제공하는 PAsswordEncoder의 종류

    /**
     * @BCryptPasswordEncoder: 암호화된 문자열이 매번 다르게 생성되도록 하여, 보안성을 높이는 데에 사용됩니다.
     * @NoOpPasswordEncoder: 아무런 암호화도 하지 않습니다. 이전 버전의 Spring Security에서 사용되었으나, 보안성이 낮아 현재는 사용을 권장하지 않습니다.
     * @Pbkdf2PasswordEncoder: PBKDF2(Password-Based Key Derivation Function 2)를 사용하여 암호화합니다. Salt 및 iteration 수를 지정할 수 있으며, 보안성이 높은 암호화 방식입니다.
     * @SCryptPasswordEncoder: scrypt 알고리즘을 사용하여 암호화합니다. Salt 및 iteration 수를 지정할 수 있으며, 보안성이 높은 암호화 방식입니다.
     * @Argon2PasswordEncoder: Argon2 알고리즘을 사용하여 암호화합니다. 메모리-hard 함수를 사용하여 보안성이 높은 암호화 방식입니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // TODO : salt란?
    /**
     * @BCryptPasswordEncoder() : 스프링 시큐리티에서 제공하는 PasswordEncoder 중 하나. 암호화된 문자열이 매번 다르게 생성되도록 하여 보안성을 높이는데 사용.salt를 사용하여 입력된 문자열을 암호홤
     * @salt란? :무작위로 생성되는 값. 이를 사용하여 문자열이 매번 다르게 생성됨. BCryptPasswordEncoder는 암호화된 문자열에 대한 검증을 지원
     * @사용방법 : BCryptPasswordEncoder를 사용하려면 Spring Security Configuration에서 bean으로 등록.
     *         암호화할 문자열을 해당 인코더의 encode()메소드에 전달 > 암호화된 문자열 생성 > db등에 저장 가능
     *         로그인 시 입력한 패스워드를 해당 인코더의 matches()메소드에 전달하여 저장된 암호화된 문자열과 비교하여 일치여부 확인
     */


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring() // 코드 분석 (web) -> web.ignoring() : WebSecurityCustomizer를 정의하는 메소드에서 사용되는 람다식
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//         h2-console 사용 및 resources 접근 허용 추가로 설정할때 활성화.
//                .antMatchers("/h2-console/**"); // h2-console에 대한 요청 무시

    }
    // TODO : WebSecurityCustomizer란? 그리고 구현가능한 메서드 종류
    /**
     * WebSecurityCustomizer는 Spring Security에서 제공하는 인터페이스 중 하나로, Spring Security의 WebSecurity 구성을 개발자가 정의할 수 있도록 해주는 역할
     * 이 클래스에서는 애플리케이션에 필요한 Bean들을 정의하고, 의존성을 주입하여 컴포넌트 간의 연결을 설정
     * 보안설정을 개발자가 직접하여서 보안검사를 하지 않아도 되는 정적 리소스를 지정해주고 서버의 부하를 줄이도록 하는 코드
     */

    // TODO : .ignoring()와 .requestMatchers() 메서드의 역할과 차이
    /**
     * @.ignoring() : Spring Security에서 보안검사를 수행하지 않아도 되는 요청 패턴을 지정하는데 사용.
     *               정적 리소스는 서버에서 동적으로 생성하지 않고, 파일 시스템에서 직접 읽어오는 자원으로, HTML, CSS, JavaScript, 이미지 파일 등이 해당
     *               이러한 자원들은 클라이언트에게 바로 제공되므로, 인증이 필요하지 않음.
     *               인증이 필요하지 않은 자원에 대한 요청 패턴을 지정해 서버의 부하를 줄이고 성능을 향상시킬 수 있음.
     *               return (web) -> web.ignoring()는 사실 아무런 동작도 하지 않음. 스프링 시큐리티에서 기본적으로 제공하는 설정이기 때문.
     *               이 코드를 제거해도 기본 보안검사가 수행되는 것은 동일. 모든 요청에 대한 보안검사가 수행되면 어플리케이션 성능저하를 유발할 수 있으므로 주의가 필요.
     * @.requestMatchers() : Spring Security에서 보안검사를 수행해야 하는 요청패턴을 지정하는데 사용
     *                      예를 들어 hasRole() 메소드와 함께 사용하여 특정 역할이 있는 사용자만 특정 자원에 액세스할 수 있도록 지정할 수 있음
     *                      HttpSecurity를 반환객체로 사용. 메소드 체이닝 가능.
     * @PathRequest : 정적 리소스에 대한 요청을 처리하는 유틸리티 클래스
     *                toStaticResources(): 정적 리소스 요청에 대한 패턴을 지정
     *                toH2Console(): H2 데이터베이스 콘솔에 대한 요청 패턴을 지정
     *                toAnyDir(): 디렉토리에 대한 요청 패턴을 지정
     *                PathRequest 클래스는 org.springframework.boot.autoconfigure.security.servlet.PathRequest 패키지에 위치
     * @.atCommonLocations() :Spring Boot에서 정의한 공통적인 정적 리소스 위치에 대한 요청에 대해서도 보안 검사를 수행하지 않도록 설정
     *
     */


    // TODO : 메소드 체이닝이란? 객체지향프로그래밍에서 사용되는 기술중 하나
    // 하나의 메소드가 반환하는 객체를 this로 호출하여 메소드를 연속적으로 호출할 수 있도록 하는 것.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); //CSRF 보호 기능을 비활성화

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests() // 요청에 대한 권한부여를 설정
                .antMatchers(HttpMethod.POST, "/api/users/signup").permitAll()   // HTTP POST 메서드로 "/api/users/signup" 경로로 들어오는 요청은 인증 없이 접근 가능
                .antMatchers(HttpMethod.POST, "/api/users/login").permitAll()    // HTTP POST 메서드로 "/api/users/login" 경로로 들어오는 요청은 인증 없이 접근 가능
                .anyRequest().authenticated() //나머지 요청은 모두 인증을 거쳐야 접근 가능
                // JWT 인증/인가를 사용하기 위한 설정 (JWT 인증/인가를 위한 커스텀 필터를 추가)
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
                //처음 로그인할때는 UsernamePasswordAuthenticationFilter를 통해 아이디,비밀번호가 필요. 그 다음에는 JWT필터를 통해 토큰만있어도 접근가능

        http.cors(); //CORS 활성화 -> 활성해줘야지만 프런트에서 백엔드 서버에 접근할 수 있음

        return http.build(); // SecurityFilterChain 객체를 빌드하여 반환
    }

    /**
     * @SecurityFilterChain : pring Security에서 요청에 대한 보안 필터 체인을 구성하는 인터페이스
     * @HttpSecurity : Spring Security에서 HTTP 요청에 대한 보안 설정을 제공하는 클래스
     *                 다양한 메소드 체인을 제공하여 HTTP 요청에 대한 인증, 인가, 보안 설정 등을 구성할 수 있음.
     *                  csrf() :CSRF 공격 방지
     *                  cors() :CORS 설정
     *                  authorizeRequests() : 요청에 대한 인가처리
     *                  formLogin(),logout() : 로그인/로그아웃 처리
     *                  sessionManagement() : 세션 관리
     *
     * @WebSecurityConfigurerAdapter :
     * @throws Exception : 해당 메소드에서 발생할 가능성이 있는 예외를 던질 수 있음을 나타내는 예약어
     *                     예약어란? : 로그래밍 언어에서 이미 문법적인 용도로 정해져 있는 키워드
     *                              예약어들은 변수명, 함수명 등으로 사용할 수 없으며, 해당 언어에서 이미 정의된 기능을 수행하도록 예약되어 있음
     *                              예약어를 변수명이나 함수명 등으로 사용하지 않도록 해야함 -> 컴파일 에러
     * // TODO : CSRF란? 그리고 JWT토큰을 사용할 때 일반적으로 비활성화 시키는 이유
     * @csrf : CSRF(Cross-Site Request Forgery) 웹 어플리케이션 취약점 중 하나로, 인증된 사용자의 권한을 도용하여 원하지 않는 요청을 실행시키는 공격
     *         사용자가 의도하지 않은 요청을 실행시켜서, 해당 웹 어플리케이션에서 발생하는 행위(글쓰기, 댓글 등록, 결제 등)를 사용자의 권한으로 대신 수행
     * @비활성화하는이유 : CSRF 공격이 CSRF 토큰을 탈취하여 요청을 위조하는 방식으로 이루어지는데
     *                JWT 토큰은 헤더와 페이로드에 정보를 저장하기 때문에 CSRF 공격에 취약하지 않기 때문.
     * @sessionCreationPolicy() : 세션 관리 정책을 설정하는 메소드
     * @SessionCreationPolicy.STATELESS :Stateless 세션 관리,서버 측에서 상태를 유지하지 않고, 요청이 들어올 때마다 인증 정보를 검증하여 처리
     * @stateless를사용하는이유 : 상태를 저장하지 않기 때문에 확장성이 높고, 클라이언트와 서버 간의 상호작용이 간소화하기 되기 때문
     * @.and().addFilterBefore() : Spring Security에서 Filter를 등록하는 메소드. 파라미터로 들어오는 해당필터를 다른 필터 앞에 실행하도록 설정 할 수 있음.
     * @JwtAuthFilter :  JWT 인증을 처리하기 위한 필터(Filter),HTTP 요청으로부터 JWT 토큰을 추출한 뒤, 이 토큰이 유효한지 검증
     * // TODO : CORS란?
     * @CORS : CORS(Cross-Origin Resource Sharing)는 웹 브라우저에서 실행되는 JavaScript가 다른 출처(도메인, 프로토콜, 포트)의 리소스에 접근하는 것을 제한하는 보안 정책
     *         기본적으로 브라우저는 보안상의 이유로 출처가 다른 리소스에 접근하려는 요청을 차단
     *         웹 브라우저에서 실행되는 클라이언트 코드가 다른 출처의 서버로부터 자원을 요청할 때 발생하는 보안 상의 문제를 해결하기 위한 매커니즘
     * @http.cors() : Cross-Origin Resource Sharing (CORS)를 활성화하는 Spring Security 구성 메서드
     *
     * // TODO : cors()메소드를 활성화한다는 것의 의미
     * 서버측에서 해당 정책을 완화하여 웹 브라우저에서 다른 출처의 리소스에 접근할 수 있도록 허용하는것.
     * 이를 통해 클라이언트에서 다른 도메인으로 API요청을 보낼 수 있게 되어 서로 다른 도메인간의 자원 공유가 가능해진다. (프런트와의 연결에서 핵심)
     * 기본적으로 Same Origin Policy 정책때문에 출처가 다른 리소스에 대한 접근이 차단됨 > cors를 활성화하지 않으면 출처가 다른 리소스에 대한 접근이 차단되는 이유
     *
     * // TODO : Same Origin Policy (웹 보안 모델 중 하나)
     * XMLHttpRequest 객체가 다른 출처(프로토콜, 호스트, 포트 중 하나라도 다른 경우)의 문서나 리소스에 접근하는 것을 제한하는 보안 정책
     * 같은 출처에서만 서로 데이터를 주고받을 수 있도록 하는 정책. XSS와 CSRF공격 등을 방지하는 중요한 보안 수단 중 하나.
     * // TODO : XSS와 CSRF에 대해 알아보기
     *
     * @.build() : HttpSecurity 객체를 이용하여 SecurityFilterChain 객체를 빌드
     * // TODO : 객체를 빌드한다는 것의 의미
     * 객체를 생성하고 초기화한 뒤, 외부에서 사용할 준비가 된 상태로 반환하는 것
     * 보통 객체를 생성하고 초기화하는 과정에서 복잡한 작업이 필요한 경우가 많은데, 이런 작업들을 빌드 메서드에서 처리하고 외부에 공개할 수 있는 인스턴스를 반환하면,
     * 객체를 사용하는 측에서는 빌드 메서드만 호출하면 되므로 객체 생성 및 초기화 작업에 대한 복잡성을 감소시킬 수 있음.
     * 반환된 객체는 스프링 시큐리티 내부에서 사용하는 것임.
     * 객체를 빌드하는 과정에서는 불변성(immutability)을 유지하면서 객체를 생성하는 것이 좋음
     * .build()로 생성된 불변 객체는 안전하게 공유될 수 있으므로 다중 스레드 환경에서도 안전하게 사용될 수 있음.
     *
     * // TODO : 빌더패턴이란?
     * 빌더 패턴은 객체 생성을 위한 코드를 간결하고 가독성 좋게 작성하면서도 객체 생성에 필요한 매개변수의 유연한 처리를 가능하게 함.
     * 이를 위해 일반적으로 빌더 클래스를 만들고, 빌더 클래스에서 필요한 매개변수들을 설정하는 메서드들을 제공하며, 최종적으로 build() 메서드를 호출하여 완성된 객체를 반환
     *
     */

    // Cors 이슈
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config = new CorsConfiguration();

        // 사전에 약속된 출처를 명시
        config.addAllowedOrigin("http://localhost:3000");

        // 프론트 웹 주소 허용
//         config.addAllowedOrigin("http://mini-prject-team3.s3-website.ap-northeast-2.amazonaws.com");

        // 특정 헤더를 클라이언트 측에서 사용할 수 있게 지정
        // 만약 지정하지 않는다면, Authorization 헤더 내의 토큰 값을 사용할 수 없음
        config.addExposedHeader("Authorization");

        // 본 요청에 허용할 HTTP method(예비 요청에 대한 응답 헤더에 추가됨)
        config.addAllowedMethod("*");

        // 본 요청에 허용할 HTTP header(예비 요청에 대한 응답 헤더에 추가됨)
        config.addAllowedHeader("*");

        // 기본적으로 브라우저에서 인증 관련 정보들을 요청 헤더에 담지 않음
        // 이 설정을 통해서 브라우저에서 인증 관련 정보들을 요청 헤더에 담을 수 있도록 해줍니다.
        config.setAllowCredentials(true);

        // allowCredentials 를 true로 하였을 때,
        // allowedOrigin의 값이 * (즉, 모두 허용)이 설정될 수 없도록 검증합니다.
        config.validateAllowCredentials();

        // 어떤 경로에 이 설정을 적용할 지 명시합니다. (여기서는 전체 경로)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}


/**
 * // TODO : CorsConfigurationSource
 * Spring에서 제공하는 인터페이스 중 하나로, CORS(Cross-Origin Resource Sharing) 정책을 적용하기 위한 설정 정보를 제공하는 역할
 * corsConfigurationSource()는 이 인터페이스를 구현한 객체를 생성하여 반환하는 메소드
 * @.addAllowedOrigin() : CORS(Cross-Origin Resource Sharing) 정책에서 요청을 보내는 출처(origin)를 설정하는 메소드
 * @.addExposedHeader() : 일반적으로 JWT토큰 인증을 구현할때 사용되는 메소드.
 *                        JWT토큰을 발급받은 사용하즌 토큰값을 Authorization헤더에 담아서 요청을 보내게되는데 서버 역시 인증 성공여부를 헤더에 담아 응답함.
 *                        .addExposedHeader("Authorization")는 Authorization 헤더 값을 노출해준다는 의미
 *                        명시적으로 설정해주지 않을 경우 헤더에 있는 토큰을 사용하지 못하게 됨. (브라우저 보안 정책때문)
 *                        정리 : 서버에서 클라이언트에게 반환한 응답 헤더에 대해, 해당 헤더를 클라이언트가 접근 가능하게 해주는 역할
 * @.addAllowedMethod() : CORS 정책에서 허용하는 HTTP 메소드를 허용해주는 메소드, 설정하지 않을경우 서버에 요청이 들어오지 않음. 서버에서 에러내용도 뜨지않음.
 * @.addAllowedHeader() : 허용되는 HTTP 요청 헤더를 지정하는 메서드.
 *                        토큰이 요청 헤더에 담겨 있을 경우, 해당 헤더를 허용하지 않으면 서버에서 토큰을 인식하지 못하고 인증이 실패할 수 있음
 *                        JWT토큰을 사용하려면 해당 헤더를 허용해줘야 함.
 *                        정리 : 클라이언트에서 요청 헤더에 대해 서버에서 허용하는 헤더 값을 정의하는 것
 * @.setAllowCredentials(true) : 쿠키, 인증 헤더 등 인증 관련 정보를 요청 헤더에 담아서 서버로 보낼 수 있게 해주는 설정. 기본값은 false
 *                               사용하려면 true로 해줘야함.
 * @.validateAllowCredentials() : allowCredentials를 true로 설정했을 때, allowedOrigin 값이 * (즉, 모두 허용)이 설정되는 것을 방지하기 위한 검증 메서드
 * @UrlBasedCorsConfigurationSource : URL 기반의 CORS 구성을 지원하기 위한 Spring Framework 클래스
 *                                    이 클래스는 CorsConfiguration 객체를 URL 경로에 매핑하여 특정 URL 경로에 대한 CORS 구성을 지정할 수 있도록 함.
 * @.registerCorsConfiguration() : 지정된 URL 패턴에 대한 CORS 구성을 등록하는 메서드
 *                                 CORS 설정 정보를 담은 CorsConfiguration 객체와 경로 패턴을 매핑하여 CorsConfigurationSource 타입의 객체를 반환합니다.
 *                                 이 반환된 객체는 HttpSecurity 클래스의 cors() 메서드에서 사용되어, 지정된 경로 패턴과 연결된 CORS 설정 정보를 적용합니다.
 *                                 따라서, registerCorsConfiguration() 메서드를 다른 클래스에 담는 것은 의미가 없습니다.
 *
 * // TODO : Swagger란?
 * RESTful API를 문서화하고, 테스트하고, 제공하는 도구로서, 개발자들이 API에 대한 이해도를 높이고, API를 사용하는데 필요한 정보를 제공하는 데 사용
 * @EnableSwagger2 : 어노테이션을 붙여야 활성화 가능
 * springfox-swagger 라이브러리를 설치한 뒤에 사용 가능
 * # Swagger
 * springfox.documentation.swagger.v2.path=/swagger
 * springfox.documentation.swagger.ui.version=2.9.2
 * springfox.documentation.swagger.ui.enabled=true
 */