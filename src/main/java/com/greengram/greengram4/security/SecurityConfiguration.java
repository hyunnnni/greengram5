package com.greengram.greengram4.security;

import com.greengram.greengram4.security.oauth2.CustomOAuth2UserService;
import com.greengram.greengram4.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.greengram.greengram4.security.oauth2.OAuth2AuthenticationRequestBasedOnCookieRepository;
import com.greengram.greengram4.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {//자바 2 p.794

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationRequestBasedOnCookieRepository oAuth2AuthenticationRequestBasedOnCookieRepository;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.sessionManagement(seesion -> seesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//세션을 사용하지 않겠다는 처리
                .httpBasic(http -> http.disable())// 기본적으로 시큐리티는 백엔드에서도 화면의 ui를 만든다(로그인) 필요없어서 끔
                .formLogin(form->form.disable())
                .csrf(csrf ->csrf.disable())//스프링에서 제공해주는 보안기법인데 화면이 없으면 쓸 필요가 없어서 사용하지 않겠다라고 처리
                .authorizeHttpRequests(auth-> auth.requestMatchers(
                                "/api/feed", "/api/feed/comment"
                                ,"/api/dm"
                                ,"/api/dm/msg")
                                .authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/user/signout", "api/user/follow")
                                .authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/user").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/user/pic").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/feed/fav").hasAnyRole("ADMIN")// 여러 개의 롤을 넣을 수 있다
                                .anyRequest().permitAll()
                                //matchers 매칭 시키는 것 permitAll 무사 통과 시키겠다 >> 적은 url을 제외하여 접근을 할 수 있게 하겠다는 뜻
                //패키지/** 한 후 그 안에 메소드 중 mapping방식으로도 나눌 수 있다.
                //hasAnyrole(  ) 권한에 따라 접근 가능하게 하는 것
                //anyRequest().hasRole("ADMIN") 그 외에 모든 것들은 ADMIN권한을 가져야 접근이 가능하다
                //적는 순서가 중요하며 anyRequest가 마지막에 있어야한다
                //.anyRequest().authenticated()
                        // 위의 주소를 제외하고 나머지는 전부 로그인을 해야 사용이 가능하게 하는 것
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)//.class 뒤에 jwtAuthenticationFilter를 끼우겠다는 뜻
                .exceptionHandling(except -> {
                    except.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                            .accessDeniedHandler(new JwtAccessDeniedHandler());
                })
                .oauth2Login(oauth2 -> oauth2.authorizationEndpoint(auth ->
                        auth.baseUri("/oauth2/authorization")
                                .authorizationRequestRepository(oAuth2AuthenticationRequestBasedOnCookieRepository))
                        .redirectionEndpoint(redirection -> redirection.baseUri("/*/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        //인터페이스 passwordEncoder를 상속 받는 BCryptPasswordEncoder를 객체화해준다.
        //암호화를 해주는 클래스
        //이전에 service에서 만든 로직은 이 후 수정 시 하나씩 다 수정해야 하는데 이렇게 작성하면 여기서만 수정하면 된다
    }
}
