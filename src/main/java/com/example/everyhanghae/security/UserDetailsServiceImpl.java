package com.example.everyhanghae.security;

import com.example.everyhanghae.entity.User;
import com.example.everyhanghae.exception.CustomErrorCode;
import com.example.everyhanghae.exception.CustomException;
import com.example.everyhanghae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // TODO : 인터페이스란? 구현체란?
    /**
     * 인터페이스 : 객체 지향 프로그래밍에서 인터페이스는 클래스가 구현해야 할 메서드의 목록을 정의
     *           인터페이스는 일종의 약속. 인터페이스는 메서드의 이름, 반환 타입, 매개 변수 등을 정의하고, 구현체는 인터페이스에서 정의한 메서드를 반드시 구현해야 함
     * 구현체 : 인터페이스에서 정의한 메서드를 실제로 구현하는 클래스
     *        인터페이스에 정의된 메서드를 실제로 실행하는 코드를 가지고 있음. 구현체는 인터페이스의 모든 메서드를 구현해야함.
     * 인터페이스와 구현체를 사용하면, 유지보수 및 확장성 측면에서 매우 유리함.
     * 인터페이스를 수정하지 않고, 구현체만 변경하여 새로운 기능을 추가하거나 수정할 수 있음
     */

    // TODO : UserDetailsService 인터페이스를 사용하는 이유
    /**
     * @UserDetailsService : UserDetailsService 인터페이스는 로그인 처리를 위한 인터페이스
     *                       이 인터페이스에서는 로그인 아이디를 매개변수로 받아서, 해당 아이디의 사용자 정보를 조회하는 메서드가 정의되어 있음
     *                       메서드가 단 하나만 존재
     *                       이 인터페이스를 사용하는 이유는 Spring Security에서 UserDetailsService를 사용하여 사용자 인증 및 권한 부여를 처리할 수 있도록 하는 것
     *                       UserDetailsService를 구현하는 사용자 지정 클래스를 작성하면 Spring Security가 이를 자동으로 감지하고 인증 및 권한 부여를 수행할 수 있음
     *                       따라서 개발자가 별도의 인증 및 권한 부여 메커니즘을 구현할 필요가 없어짐
     * @loadUserByUsername : 사용자 이름을 기반으로 UserDetailsService 구현체에서 사용자를 로드하고 UserDetails 객체를 반환
     *                       UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
     *
     */



    private final UserRepository userRepository;

    @Override //메서드를 재정의하여 사용
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user);
    }

}
// TODO : UserDetails 타입이 아니라 UserDetailsImpl타입으로 반환하는 이유, 처음부터 UserDetailsImpl를 반환타입으로 정하기 못하는 이유
/**
 * UserDetailsImpl 클래스가 UserDetails 인터페이스를 구현(implements)하기 때문에 UserDetailsImpl 객체는 UserDetails 타입으로 사용될 수 있음.
 * 인터페이스에 대한 반환타입으로 구현체 클래스를 타입으로 사용할 수 있음.
 * 인터페이스는 클래스가 아니라 메서드를 정의해놓은 인터페이스이기 때문임.
 * 인터페이스는 소속되어있는 메서드를 구현하는 구현체 클래스가 반드시 필요하고 구현체 클래스가 반환타입이 될 수 있음.
 * 인터페이스는 존재함과 동시에 구현하는 구현체 클래스가 만들어질 것을 예고하고있음 : 그렇게 사용하기 위해 인터페이스를 만드는 것임
 * 이것을 다형성이라고 함
 * 처음부터 UserDetailsImpl를 반환타입으로 정하기 못하는 이유 : 오버라이딩은 메서드와 반환타입, 파라미터가 모두 같아야 하기 때문.
 */

// TODO : 다형성(Polymorphism)이란?
/**
 * 객체 지향 프로그래밍에서 객체가 여러 가지 형태를 가질 수 있는 능력
 * 이는 한 클래스 내에서 메서드나 변수가 다양한 형태로 사용될 수 있음을 의미
 * 다형성은 객체 지향 프로그래밍에서 가장 중요한 특징 중 하나이며, 코드의 재사용성과 유지보수성을 높이는 데에 큰 도움
 * 오버로딩(Overloading) : 같은 이름의 메서드나 생성자를 여러 개 정의하여 매개변수의 타입과 개수에 따라 다른 동작을 수행할 수 있도록 함
 *                       반드시 클래스 이름과 같음.
 *                       같은 클래스나 상속관계에 있는 클래스에서만 가능
 *                       인터페이스에 있는 메서드는 오버로딩할 수 없음(구현이 없기 때문)
 *                       인터페이스를 구현한 구현체 클래스에서도 오버로딩할 수 없음. 인터페이스에 선언된 메서드를 그대로 구현해야하기 때문.
 * 오버라이딩(Overriding) : 부모 클래스에서 정의된 메서드를 자식 클래스에서 재정의하여 자식 클래스에서 원하는 대로 동작하도록 함.
 *                       부모 클래스에 있는 메서드와 이름이 같음.파라미터의 개수와 타입, 그리고 반환 타입이 모두 같아야 함.
 *                       즉, 인터페이스에 선언된 메서드를 그대로 구현해야 함.
 *
 */