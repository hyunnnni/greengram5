package com.greengram.greengram4.security.oauth2;

import com.greengram.greengram4.common.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.greengram.greengram4.security.oauth2.OAuth2AuthenticationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final CookieUtils cookieUtils;//소셜로그인시 쿠키사용하기 때문
    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("OAuth2AuthenticationFailureHandler - onAuthenticationFailure");
        String targetUrl = cookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("/");

        exception.printStackTrace();

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("error", exception.getLocalizedMessage())
                        .build()
                        .toUriString();

        repository.removeAuthorizationRequestCookies(response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        super.onAuthenticationFailure(request, response, exception);//ctrl랑 스페이스 누르면 자동 완성 해줌
    }
}
