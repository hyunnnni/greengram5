package com.greengram.greengram4.security.oauth2;

import com.greengram.greengram4.common.CookieUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor

public class OAuth2AuthenticationRequestBasedOnCookieRepository
implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDS = 180;
    public final CookieUtils cookieUtils;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.info("OAuth2AuthenticationRequestBasedOnCookieRepository - loadAuthorizationRequest");
        return cookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> cookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);//orElse = if 만약 값이 존재했다면 위의 map으로 return이 되고 아니라면 null이 return되게 해주는 메소드
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.info("OAuth2AuthenticationRequestBasedOnCookieRepository - saveAuthorizationRequest");

        if(authorizationRequest == null){
            this.removeAuthorizationRequestCookies(response);
            return;
        }
        String serializeAuthReq = cookieUtils.serialize(authorizationRequest);

        cookieUtils.setCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
                , serializeAuthReq, COOKIE_EXPIRE_SECONDS);

        String redirectUriAfterLogin = request.getParameter(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        //getParameter : 쿼리스트링에 값이 없으면 바디 안까지 찾아준다
        //OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME 이 이름으로 값을 빼서 redirectUriAfterLogin에 넣어준다

        log.info("redirectUriAfterLogin : {}", redirectUriAfterLogin);

        if (StringUtils.isNotBlank(redirectUriAfterLogin)){//그 값이 빈값이 아니라면 Cookie에 이렇게 값을 넣어라
            cookieUtils.setCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME
            , redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("OAuth2AuthenticationRequestBasedOnCookieRepository - removeAuthorizationRequest");
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletResponse response){
        log.info("OAuth2AuthenticationRequestBasedOnCookieRepository - removeAuthorizationRequestCookies");

        cookieUtils.deleteCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        cookieUtils.deleteCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
