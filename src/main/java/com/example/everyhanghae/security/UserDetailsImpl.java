package com.example.everyhanghae.security;

import com.example.everyhanghae.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    // ==============여기 아래로는 사용하지는 않지만 반드시 구현해야하기 때문에 만든 오버라이딩한 메서드들
    // TODO : 인터페이스에서를 구현하는 구현체 클래스에서는 반드시 인터페이스의 모든 메서드를 구현해야한다.
    /**
     * 인터페이스는 메서드의 시그니처(메서드 이름, 매개변수 및 반환유형)만을 정의함
     * 실제로 메서드를 실행하는 코드는 인터페이스를 구현하는 클래스에서 작성해야한다.
     * 인터페이스에 선언된 모든 메서드를 구현하지 않으면 컴파일 오류가 발생함.
     * 그래서 사용하지 않더라도 오버라이딩을 해주고 반환값을 없게하는 것.
     */


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // TODO : <? extends GrantedAuthority>

    /**
     * Java에서 와일드카드 타입의 일종
     * "GrantedAuthority를 상속하는 어떤 클래스든 상관없이 모두 수용할 수 있다는 의미
     * 이 메서드에서 반환하는 Collection은 GrantedAuthority의 하위 클래스로 구현되어도 상관없음
     * 컴파일 타임에 유연성을 높이고, 런타임시 다형성을 보장
     * @Collection : Java의 인터페이스 중 하나로, 객체들의 그룹을 담는 컨테이너 역할을 함
     *               여러 개의 객체를 저장하고 관리하기 위한 메서드들이 정의되어 있으며, 구현체로는 List, Set이 있음
     *               Collection 인터페이스를 구현한 구현체들은 다형성을 지원하기 때문에
     *               같은 Collection 타입으로 변수를 선언하여 다양한 구현체를 참조할 수 있음
     *               여러 개의 요소를 담을 수 있는 자료구조를 의미
     *               Collection은 기본적으로 Object를 요소로 가질 수 있음. 따라서 모든 객체를 담을 수 있는 자료구조.
     *               Set : Set 인터페이스는 중복을 허용하지 않기 때문에 중복된 요소는 추가되지 않음
     *               List : List 인터페이스는 인덱스로 요소에 접근할 수 있기 때문에, 요소가 순서를 가지고 있음
     *               Collection은 모든 객체를 담을 수 있지만, 구현체에 따라서는 특정한 제한을 둘 수 있음
     */

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } //계정 만료

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } //계정 잠김

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } //계정의 자격 증명(보통 비밀번호)이 만료되었는지 여부를 반환하는 메서드

    @Override
    public boolean isEnabled() {
        return true;
    } // 메서드는 계정이 사용 가능한지를 나타내는 boolean 값을 반환
    //이 메서드를 구현하는 클래스에서는 해당 계정의 사용 가능 여부를 판단하는 로직을 구현해야 함.
    // 예를 들어, 계정의 활성화 여부를 데이터베이스에서 조회하거나, 사용자가 입력한 정보를 검증하여 계정의 사용 가능 여부를 판단할 수 있음.
}