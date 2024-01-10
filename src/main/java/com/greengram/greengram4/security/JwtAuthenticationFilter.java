package com.greengram.greengram4.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { //요청이 들어올 때 마다 실행이 되며 request에 토큰이 들어오는지 확인한다.
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);//값이 넘어오면 로그인한 사용자 null이면 로그인한 사용자가 아니다
        log.info("JwtAuthentication-Token : {}", token);

        if(token != null && jwtTokenProvider.isValidateToken(token)){
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            if(auth != null){
                SecurityContextHolder.getContext().setAuthentication(auth);//로그인 사용자라면 값을 담아준다
            }
        }
        filterChain.doFilter(request, response);//사용하고 다시 다음 필터에 값을 넘겨줌 null이든 아니든 무조건 실행되어야하는 부분
    }
}
