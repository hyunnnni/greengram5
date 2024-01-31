package com.greengram.greengram4.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component//빈등록
public class CookieUtils {
    public Cookie getCookie(HttpServletRequest request, String name){//request에 담긴 쿠키 가져오기
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(name.equals(cookie.getName())){
                    return cookie;
                }
            }
        }
        return null;
    }
    public void setCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");//주소값 기준으로 쿠키가 만들어진다
        cookie.setHttpOnly(true);//true
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);// 브라우저 당 생성이 되고 쿠키가 올 때 마다 담겨서 오게된다.

    }
    public void deleteCookie(HttpServletResponse response, String name) {//로그아웃 시 쿠키 삭제
        Cookie cookie = new Cookie(name,null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

    }

}
