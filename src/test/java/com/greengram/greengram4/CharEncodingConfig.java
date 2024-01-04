package com.greengram.greengram4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class CharEncodingConfig {//한글 깨짐 방지 mockmvcConfig와 같은 역할이라 둘 중 하나만 사용해도 됨
    //이게 적는 양이 더 적어서 이걸 사용하는 게 더 나은듯
    @Bean
    public CharacterEncodingFilter characterEncodingFilter(){
        return new CharacterEncodingFilter("UTF-8", true);
    }
}
