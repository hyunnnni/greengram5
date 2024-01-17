package com.greengram.greengram4.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public MyUserDetails getLoginUser(){
        return (MyUserDetails)SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();// 강제형변환 Authentication가 UserDetails 상속
        //Principal에서 iuser를
    }

    public int getLoginUserPk() {
        MyUserDetails myUserDetails = getLoginUser();
        return getLoginUser() == null ? 0 : myUserDetails
                .getMyPrincipal()
                .getIuser();
    }
}
