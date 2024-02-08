package com.greengram.greengram4.security;

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

    private MyPrincipal myPrincipal;
    private Map<String, Object> attributes;
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
        return null;
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
