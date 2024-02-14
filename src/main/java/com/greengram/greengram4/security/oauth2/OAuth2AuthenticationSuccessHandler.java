package com.greengram.greengram4.security.oauth2;

import com.greengram.greengram4.common.AppProperties;
import com.greengram.greengram4.common.CookieUtils;
import com.greengram.greengram4.security.JwtTokenProvider;
import com.greengram.greengram4.security.MyPrincipal;
import com.greengram.greengram4.security.MyUserDetails;
import com.greengram.greengram4.user.model.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.greengram.greengram4.security.oauth2.OAuth2AuthenticationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request,response,authentication);
        log.info("targetUrl : {}",targetUrl);
        if(response.isCommitted()){
            log.debug("Response has already been committed. Unable to redirect to {}",targetUrl);
            return;
        }

        clearAuthenticationAttributes(request,response);
        getRedirectStrategy().sendRedirect(request,response,targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) {
        Optional<String> redirectUri = cookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if(redirectUri.isPresent() && !hasAuthorizedRedirectUri(redirectUri.get())){
            throw new IllegalArgumentException("Sorry!, Unauthorized Redirect URI");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());//redirectUri가 값이 있었다면 targetUrl에 그 값을 넣고 아니라면 부모의 디폴트값을 넣을거다

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        MyPrincipal myPrincipal = myUserDetails.getMyPrincipal();

        String at = jwtTokenProvider.generateAccessToken(myPrincipal);
        String rt = jwtTokenProvider.generateRefreshToken(myPrincipal);

        int rtCookieMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie( response, "rt");
        cookieUtils.setCookie(response, "rt", rt, rtCookieMaxAge);

        UserModel userEntity = myUserDetails.getUserEntity();

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", at)
                .queryParam("iuser", userEntity.getIuser())
                .queryParam("nm", userEntity.getNm()).encode()
                .queryParam("pic", userEntity.getPic())
                .queryParam("firebase_token", userEntity.getFirebaseToken())
                .build()
                .toUriString();

    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        super.clearAuthenticationAttributes(request);
        repository.removeAuthorizationRequestCookies(response);
    }

    private boolean hasAuthorizedRedirectUri(String uri){//넘어오는 uri값이 yaml에 잇는 oauth2 중에 있는지 체크하는 작업
        URI clientRedriectUri = URI.create(uri);
        log.info("clientRedriectUri.getHost(): {}", clientRedriectUri.getHost());
        log.info("clientRedriectUri.getPort(): {}", clientRedriectUri.getPort());

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(redirectUri -> {
                    URI authorizedURI = URI.create(redirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedriectUri.getHost())//yaml에 있는 : 전까지 주소가 host값
                    && authorizedURI.getPort() == clientRedriectUri.getPort()){
                        return true;
                    }
                    return false;
                });
    }
}
