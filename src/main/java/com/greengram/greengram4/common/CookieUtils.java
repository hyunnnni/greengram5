package com.greengram.greengram4.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Optional;

@Component//빈등록
public class CookieUtils {
    public Optional<Cookie> getCookie(HttpServletRequest request, String name){//request에 담긴 쿠키 가져오기
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(name.equals(cookie.getName())){
                    return Optional.of(cookie);
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

    public String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    public <T> T deserialize(Cookie cookie, Class<T> cls){//직렬화를 반대로 json을 객체로
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }

}
