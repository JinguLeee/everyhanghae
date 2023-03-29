package com.example.everyhanghae.jwt;

import com.example.everyhanghae.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * @Component :스프링 프레임워크에서 bean으로 등록하고 관리되어야 하는 클래스를 표시하는데 사용
 *             스프링 컨테이너가 해당 클래스를 검색하고 관리하게 해주는 스프링의 컴포넌트 스캔 기능과 함께 사용됨
 *             이렇게 등록된 bean은 다른 bean에서 사용될 수 있고, 의존성 주입(DI) 등의 스프링의 다양한 기능을 사용할 수 있게 됨.
 *
 *
 */
@Slf4j //Lombok 라이브러리에서 제공하는 어노테이션으로, 로깅을 위한 코드를 간단하게 작성할 수 있도록 도와줌
@Component //Spring Framework에서 컴포넌트 스캔을 하여 해당 클래스를 Bean으로 등록하도록 지정
@RequiredArgsConstructor //Lombok 라이브러리에서 제공하는 어노테이션으로, 필드에 대한 생성자를 자동으로 생성해줌. 이렇게 생성된 생성자는 클래스에 final 키워드가 붙은 필드만 인자로 받을 수 있음.
public class JwtUtil {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    /**@RequiredArgsConstructor 가 없으면 DI가 이루어지지 않음.NullPointerException 등의 에러가 발생
     * public JwtUtil(UserDetailsServiceImpl userDetailsService) {
     *         this.userDetailsService = userDetailsService;
     *     }
     */

    // TODO : ragsConstructor종류
    /**
     * @AllArgsConstructor: 모든 필드를 인자로 받는 생성자를 자동으로 생성,의존성 주입 등에서 사용
     * @RequiredArgsConstructor: final로 선언된 필드만을 인자로 받는 생성자를 자동으로 생성,의존성 주입 등에서 사용
     * @NoArgsConstructor: 인자를 받지 않는 디폴트 생성자를 자동으로 생성, JPA에서 엔티티 클래스를 작성할 때 사용
     */

    public static final String AUTHORIZATION_HEADER = "Authorization";
    // HTTP 헤더에서 토큰을 담을 때 사용되는 필드명(key) 일반적으로 "Authorization"으로 사용
    // Authorization
    private static final String BEARER_PREFIX = "Bearer ";
    //토큰을 HTTP 헤더에 담을 때, "Bearer "라는 문자열과 함께 토큰을 담는 것이 일반적
    private static final long TOKEN_TIME = 60 * 60 * 1000L;
    //토큰이 유효한 기간을 밀리초 단위로 지정하는 상수

    // TODO : 토큰에 들어가는 값을 static으로 정하는 이유
    /**
     *  static이 붙은 자료는 클래스 로더에 의해 메모리의 static 영역에 로드되기 때문에, 인스턴스 생성과 무관하게 메모리에 상주
     *  따라서 인스턴스를 생성하지 않아도 바로 접근이 가능하며 메모리에 올라간 상태에서 프로그램 전체에서 공유됨
     *  static 키워드를 사용하면 객체를 생성하지 않고 클래스명으로 바로 접근할 수 있음.
     *  따라서, HTTP 헤더에서 토큰을 담을 필드명은 고정된 값이므로 static으로 선언하여 다른 코드에서도 쉽게 접근할 수 있도록 함.
     */

    // TODO : static메모리 영역은 JVM이 관리한다.
    /**
     * static 영역은 JVM(Java Virtual Machine)이 관리함
     * static 영역은 프로그램이 실행될 때 자동으로 초기화 됨.클래스가 JVM에 로딩될 때 생성. 메모리 할당도 JVM이 자동으로 수행
     * 개발자가 별도로 메모리 할당과 해제를 할 필요가 없음
     * 로그인한 모든 유저의 토큰 자료는 static 메모리 저장하고 JVM이 관리한다고 보면 됨
     * static 메모리는 프로그램이 종료될 때까지 계속 유지되므로, 많은 로그인 사용자가 있을 경우 메모리 부족 문제가 발생할 수 있음
     * 이러한 경우에는 토큰 정보를 DB나 다른 곳에 저장하고, 필요할 때마다 DB나 해당 저장소에서 정보를 가져와서 사용하는 방법을 고려할 수 있음(일반적이지 않음)
     * AWS 서버에서도 Java 애플리케이션을 실행하는 경우, 애플리케이션 서버가 종료되거나 다시 시작될 때마다 애플리케이션의 모든 메모리(Heap, Static 등)는 리셋됨.
     * JWT 토큰이 static 메모리에 저장되는 경우, 유효 시간이 지나면 자동으로 삭제 >서버가 불필요한 데이터로 인해 메모리 부족 문제가 발생하는 것을 방지
     */

    // TODO : Java Virtual Machine(JVM)
    /**
     *  Java 언어로 작성된 애플리케이션을 실행하고 관리하는 런타임 환경
     *  JVM은 Java 언어로 작성된 코드를 컴파일하여 기계어로 변환하고, 실행 중에 메모리 관리 및 가비지 컬렉션과 같은 기능을 제공
     *  static 메모리 영역을 관리하고 토큰 정보를 유지함.
     */


    @Value("${jwt.secret.key}")
    private String secretKey;
    //@Value : 주입받을 값을 외부 설정 파일(application.yml 또는 application.properties)에서 가져와서 변수에 주입

    private Key key;
    //Key 객체는 JWT 토큰의 서명(signature)을 검증하거나 생성하는 데 사용됨
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    // TODO : SignatureAlgorithm
    /**
     *  Key 객체는 JWT 토큰의 서명(signature)을 검증하거나 생성하는 데 사용됨
     *  RS256, ES256 등 다양한 서명 알고리즘을 선택가능
     *  WT 토큰은 헤더와 페이로드로 구성되며, 이를 서명하여 검증이 가능하게 만든다. 이때 사용하는 서명 알고리즘은 헤더의 alg 필드에 정의
     *  HS256알고리즘 :SHA256 해시 함수를 사용하여 메시지를 서명하고 검증,서명 알고리즘은 일반적으로 대칭키 암호화를 사용하여 메시지 무결성을 보호하기 때문에 안전
     *              비밀키가 노출되면 취약해질 수 있습니다. 따라서 비밀키 관리에 유의해야 함
     */

    // TODO : secretKey(비밀키)가 있어야 JWT토큰 발급 가능
    /**
     *  secretKey(비밀키) : JWT 토큰은 서버와 클라이언트 간에 암호화된 메시지를 전달하기 위한 것이므로, 비밀키 없이 암호화를 수행할 수는 없음.
     *                    따라서, 서버에서 비밀키를 갖고 있어야 하며, 해당 비밀키를 이용하여 JWT 토큰을 생성하고 검증함.
     *  비밀키 관리 : 일반적으로는 서버의 환경변수에 비밀키를 저장하고 관리.
     *             클라우드 서비스를 사용하는 경우 해당 클라우드 서비스에서 제공하는 암호화된 저장소를 이용하여 비밀키를 안전하게 보관 가능
     *  aws에서 비밀키 관리하는 법: AWS System Manager Parameter Store나 AWS Secrets Manager 같은 서비스를 이용해 비밀키를 관리하는 것을 권장
     *                        AWS Identity and Access Management(IAM)을 이용해 해당 서비스에 대한 권한을 관리할 수 있음
     *                        AWSSimpleSystemsManagement 인터페이스를 사용하여 파라미터를 요청해야 함.
     */

    // TODO : aws에서 비밀키 관리하는 법
    /**
     * AWS System Manager Parameter Store나 AWS Secrets Manager 같은 서비스를 이용해 비밀키를 관리하는 것을 권장
     * AWS Identity and Access Management(IAM)을 이용해 해당 서비스에 대한 권한을 관리할 수 있음
     * AWSSimpleSystemsManagement 인터페이스를 사용하여 파라미터를 요청해야 함.
     */

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); //secretKey 문자열을 Base64 디코딩하여 byte[] 배열로 변환
        key = Keys.hmacShaKeyFor(bytes);
    }

    // TODO : @PostConstruct 어노테이션
    /**
     * @PostConstruct 어노테이션은 스프링 빈의 초기화 메서드를 지정할 때 사용
     * 이 어노테이션이 지정된 메서드는 해당 빈의 의존성이 모두 주입된 후에 자동으로 실행됩니다. 이때 해당 빈이 사용될 준비가 되도록 초기화 작업을 수행
     * JwtUtil 빈이 생성된 후 init() 메서드가 자동으로 호출됨
     * @.getDecoder() : Base64 클래스의 메소드 중 하나. Base64로 인코딩된 데이터를 디코딩할 수 있음.이 때 반환되는 객체는 Base64.Decoder 클래스의 인스턴스
     * @.hmacShaKeyFor() : javax.crypto.spec.SecretKeySpec 클래스의 인스턴스를 생성.HMAC을 위한 비밀 키를 생성하기 위해 사용
     *                     hmacShaKeyFor() 메서드는 SecretKey를 반환하는데, 이는 HMAC을 계산하기 위해 사용되는 키 자료를 제공
     */

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        //HttpServletRequest 객체에서 'Authorization' 헤더 필드에 담겨진 JWT 토큰 값을 추출하는 코드
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            //StringUtils.hasText() : 메소드는 주어진 문자열이 null이 아니거나 공백 문자가 아닐 때 true를 반환
            //                        HTTP 요청 헤더에서 Authorization 필드가 존재하고 그 값이 null이 아니거나 공백 문자가 아닐 때 true를 반환
            //bearerToken.startsWith() : 주어진 문자열이 BEARER_PREFIX로 시작하는지를 확인
            //                           HTTP 요청 헤더에서 Authorization 필드가 BEARER_PREFIX로 시작하는 JWT 토큰을 가지고 있을 때 true를 반환
            return bearerToken.substring(7);
            //substring(7)은 문자열에서 "Bearer "를 제외한 나머지 문자열을 추출. 띄어쓰기까지 포함하여 7자.추출된 문자열은 JWT 토큰 값이 됨.
            //BEARER_PREFIX : JWT 토큰의 앞에 붙는 문자열로, "Bearer " 문자열을 나타냄.
        }
        return null;
        //추출된 JWT 토큰이 없으면 이후에 인증 작업을 수행할 수 없으므로 null을 반환
        //null을 반환하면 호출한 쪽에서 null 값을 받게 되고, 이 경우에는 유효한 토큰이 없는 상황으로 처리
    }
    // TODO : HttpServletRequest란?
    /**
     * @HttpServletRequest : Java Servlet에서 HTTP 요청을 처리하기 위해 사용되는 인터페이스
     *                       HTTP 요청 메시지를 캡슐화하고, 클라이언트가 전송한 데이터 및 요청 헤더 정보에 대한 접근을 제공
     *                       HttpServletRequest는 Servlet container에 의해 생성되며, 클라이언트가 전송한 HTTP 요청을 나타냄
     *                       이 인터페이스를 사용하면, HTTP 메소드, URL, 헤더, 쿠키 및 요청 파라미터와 같은 다양한 요청 정보에 접근가능
     *                       서버가 클라이언트에게 응답을 보낼 수 있는 HttpServletResponse 객체를 생성할 수 있음
     */

    // TODO : Servlet container란? 서블릿 컨테이너
    /**
     * @Servlet_container : 웹 어플리케이션 서버(WAS)에서 Servlet을 관리하고, Servlet의 생명주기를 관리하며, HTTP 요청에 대한 로직을 처리하는 컴포넌트(Bean)
     *                      예를 들어, 클라이언트가 HTTP 요청을 보내면 Servlet container는 해당 요청을 받아서, 처리를 위해 해당 Servlet을 호출하고,
     *                      Servlet이 처리한 결과를 다시 클라이언트에게 반환하는 역할
     *                      Apache Tomcat, Jetty, JBoss 등이 Servlet container로 사용
     */
    // TODO : 컴포넌트란? = Bean
    /**
     * @Component : 모듈화된 독립적인 기능을 수행하는 소프트웨어의 일부분을 의미
     *              소프트웨어를 컴포넌트 단위로 나누어 개발하면, 코드의 재사용성과 유지보수성이 향상되며, 여러 사람이 협업하는 개발 프로젝트에서 일관된 구조와 규격을 유지하기 용이
     *              Spring Framework에서는 컴포넌트를 Bean이라는 용어로 표현
     */
    // TODO : getHeader(), startsWith(), hasText(), substring()
    /**
     * @getHeader() : HttpServletRequest 인터페이스에 정의된 메서드로, HTTP 요청 헤더에서 특정 헤더의 값을 가져오는 역할
     *                JWT 토큰을 가져올 때에는, Authorization 헤더의 값을 가져오는데 사용
     *                이 헤더의 값은 Bearer {JWT 토큰}의 형태를 가지며, Bearer라는 문자열은 인증 방식을 나타내는 키워드
     * @startsWith() : String 클래스의 메소드. 문자열이 지정된 문자열로 시작하는지 여부를 반환
     *                 주어진 문자열이 특정 접두사로 시작하는지 확인할 때 유용
     *                 비교 대상 문자열이 매개변수로 전달된 문자열로 시작하면 true를 반환하고, 그렇지 않으면 false를 반환
     * @hasText() : Spring Framework의 StringUtils 클래스에서 제공되는 정적 메서드
     *              문자열이 null이거나 공백이면 false를, 그렇지 않으면 true를 반환
     *              문자열이 null이 아니며 비어 있지 않은지 확인하기 위해 사용
     * @substring() : String 클래스의 메소드. 문자열에서 지정한 시작 위치부터 끝 위치까지의 부분 문자열을 반환하는 메서드
     *                예시 : String str = "Hello, World!"; str.substring(0, 5) Hello반환
     *                      str.substring(7)와 같이 끝 위치를 지정하지 않으면 시작 위치부터 문자열의 끝까지의 부분 문자열을 반환
     *
     */



    // 토큰 생성
    public String createToken(String loginId, String className) {
        Date date = new Date();
        Claims claims = Jwts.claims().setSubject(loginId);
        // JWT payload의 subject에 loginId 값을 설정하여 claims 객체에 담음
        claims.put("className", className);
        //claims 객체에 className이라는 이름으로 className 값을 추가하는 코드
        //이렇게 추가된 값은 JWT 토큰의 payload 부분에 담겨져 전송
        return BEARER_PREFIX +
                Jwts.builder() // Jwts.builder()를 사용해서 JWT 토큰을 생성
                        .setClaims(claims)
                        //.setClaims 메소드 : 토큰의 payload에 해당하는 Claims 객체를 설정
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        //.setExpiration 메소드 : 토큰의 만료 시간을 설정
                        //date.getTime()은 1970년 1월 1일 00:00:00 GMT부터 date 객체가 나타내는 날짜와 시간까지의 밀리초 수를 반환(현재시간)
                        //상수로 선언된 TOKEN_TIME 밀리초를 더한 시간을 나타내는 (new) Date 객체를 생성가능
                        .setIssuedAt(date)
                        // .setIssuedAt 메소드 : 토큰 발급 시간을 설정하는 메서드
                        // 이 메서드는 Date 객체를 인자로 받아 해당 시간을 발급 시간으로 설정
                        // 토큰 검증 시 이 값을 사용하여 토큰의 유효성을 판단
                        // 토큰이 발급된 이후에 토큰의 만료 시간을 확인하여 토큰이 유효한지 판단하고, 만료 시간 이전에 발급된 토큰인지도 확인
                        .signWith(key, signatureAlgorithm)
                        // .signWith 메서드 : JWT에 서명을 추가
                        // key 매개 변수는 HMAC 서명을 위해 사용될 비밀 키를 지정
                        // 비밀 키는 JWT를 생성할 때 서버에서 생성하며, 나중에 서명 검증을 위해 사용
                        // signatureAlgorithm 매개 변수는 사용될 서명 알고리즘을 지정 > 이 코드에서는 HS256 HMAC 서명 알고리즘을 사용
                        .compact();
                        //.compact() 메서드 : 생성된 JWT 토큰을 문자열로 변환하는 메소드
                        // 생성된 JWT 토큰은 JWS (JSON Web Signature) 형식을 따르며, 이 형식에 맞게 서명되어야 함
                        // 문자열로 된 JWT 토큰을 반환
    }

    // TODO : 클래스 Date , Claims ,Jwts
    /**
     * @Date : 자바에서 날짜와 시간을 처리하기 위한 클래스
     *         유닉스 시간(epoch time)을 기반으로 하며, 1970년 1월 1일 자정 이후로 경과된 밀리초를 기록하고, 이 값을 이용하여 날짜와 시간을 계산
     *         자바8부터 LocalDate, LocalTime, LocalDateTime, ZonedDateTime추가 됨.
     * @Claims : io.jsonwebtoken.Claims 클래스는 JWT 토큰에 저장되는 클레임(claims) 정보를 나타내는 클래스
     *           클레임은 JWT 토큰에 저장될 정보를 의미. 예를 들어 사용자 ID, 권한, 만료 시간 등의 정보가 저장될 수 있음
     *           claims 객체는 JWT 토큰에 추가적인 정보를 담기 위한 용도로 사용
     *           일반적으로는 인증에 필요한 사용자 정보나 권한 등을 담음
     *           이렇게 추가된 값은 JWT 토큰의 payload 부분에 담겨져 전송
     * @Jwts : JWT 토큰을 생성, 파싱, 유효성 검사하는 기능을 제공하는 클래스
     *         JWT 토큰을 생성할 때 사용되는 builder() 메서드를 제공
     *         파싱할 때 사용되는 parser() 메서드 제공
     *         유효성 검사할 때 사용되는 require() 메서드도 제공
     *         JWT를 생성하고 파싱할 때 필요한 알고리즘, 비밀키, 클레임 등을 설정할 수 있는 메서드를 제공하므로 JWT 관련 작업에 필수적인 클래스
     *
     * @setSubject() : 클레임의 주제를 설정하는 메서드
     * @put() : 메서드를 사용하여 클레임에 다른 정보를 추가할 수 있음, json형태의 객체이므로 key-value 값으로 정보를 추가
     *          이렇게 추가된 정보는 JWT를 수신하는 쪽에서 추출하여 사용할 수 있음
     * @Jwts.claims() : JWT payload에 해당하는 클레임 정보를 생성하는 메서드 , JWT payload는 JWT 토큰에 담기는 정보
     * @setSubject() : payload의 subject에 해당하는 값을 설정하는 메서드,JWT payload의 subject는 해당 JWT 토큰을 발급받는 사용자의 고유한 식별자로 사용
     * @jwts.builder() : JWT 토큰을 생성하기 위한 빌더 객체를 생성하는 메소드. 이 빌더 객체를 통해 JWT 토큰의 내용을 설정하고, 서명을 추가
     */

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            //.parserBuilder() : JWT 토큰을 파싱하기 위한 JwtParserBuilder 객체를 생성
            //.setSigningKey() : 메서드를 사용하여 JWT를 서명하는 데 사용된 키를 설정
            //.build() : 메서드를 호출하여 JWT 파서를 빌드
            //.parseClaimsJws() : 메서드를 호출하여 토큰을 검증
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            //SecurityException: 키(key)가 유효하지 않을 때 발생
            //MalformedJwtException: JWT가 올바른 형식이 아닐 때 발생
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            //ExpiredJwtException은 JWT 토큰이 만료되었을 때 발생하는 예외 클래스
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            //UnsupportedJwtException은 Jwts.parserBuilder() 메서드로 파싱할 때 사용된 서명 알고리즘이나 구조가 잘못된 경우 발생할 수 있는 예외
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException : 토큰에 대한 Claim(요구사항)이 없는 경우에 발생. 예를들어 "sub"라는 Claim에 대한 정보가 없거나 빈 문자열인 경우
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        //.parserBuilder() : JWT 토큰을 파싱하기 위한 JwtParserBuilder 객체를 생성
        //.setSigningKey() : 메서드를 사용하여 JWT를 서명하는 데 사용된 키를 설정
        //.build() : 메서드를 호출하여 JWT 파서를 빌드
        //.parseClaimsJws() : 메서드를 호출하여 토큰을 검증
        //.getBody() :  JWT 토큰에서 저장된 클레임 정보를 반환, 반환 타입은 Claims 객체
        //              클레임 정보는 보통 JWT 토큰에서 인증 정보 외에도, 예를 들어 사용자의 추가 정보나 권한 등을 저장할 때 사용

    }

    // 인증 객체 생성
    public Authentication createAuthentication(String loginId) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}