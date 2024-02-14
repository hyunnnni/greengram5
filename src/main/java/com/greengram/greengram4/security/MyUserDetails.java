package com.greengram.greengram4.security;

import com.greengram.greengram4.user.model.UserModel;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class MyUserDetails implements UserDetails, OAuth2User {
                               // 로컬 로그인에 사용, 소셜 로그인에 사용

    private MyPrincipal myPrincipal;
    private Map<String, Object> attributes;//OAuth2User를 사용시 필수 무조건 이걸 구현을 해야하기에 오류가 생기지 않는다
    //메소드를 넣어도 되고 이렇게 멤버필드로 만들어도 된다.
    private UserModel userEntity;//로그인 시 프론트에서 액세스 토큰만 받고 있는데 이 값들도 필요하기에
    //한 번의 통신으로 두 개의 값 다 받고싶기에 만든 멤버필드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {//권한이 무엇이 있느지 통과시키는 부분
        if(myPrincipal == null) {
            return null;
        }
        return this.myPrincipal.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                //.map(SimpleGrantedAuthority::new)//SimpleGrantedAuthority::new 메소드 참조 가공을 하지 않을 것이라면 이대로 사용하여도 된다
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {//로그인 시 루틴을 탄다면 사용하는 메소드 직접 패스워드와 이름을 넣어주어야 함 지금은 현재 커스터마이징으로 사용 중이라 필요없음
        return null;
    }

    @Override
    public String getUsername() {//로그인 시 루틴을 탄다면 사용하는 메소드 직접 패스워드와 이름을 넣어주어야 함 지금은 현재 커스터마이징으로 사용 중이라 필요없음

        return userEntity == null ? null : userEntity.getUid();
    }

    @Override
    public boolean isAccountNonExpired() {//메소드 이름 그대로 account가 expired가 안되어있냐 물어보는 것
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {//활성화
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
