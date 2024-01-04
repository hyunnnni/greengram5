package com.greengram.greengram4;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureMockMvc
@Import({ MockMvcConfig.MockMvc.class})
public @interface MockMvcConfig {// 한글 깨짐을 방지하는 코드

    class MockMvc{
        @Bean
        MockMvcBuilderCustomizer utf8Config(){
            return builder -> builder.addFilter(new CharacterEncodingFilter("UTF-8",true));
            // -> 이 기호는 애로우펑션,,? 함수( 메소드 )
            // 자바 스크립트에서 쓰는 문법 람다식 (근데 자바 스크립트에선 =>으로 적는다)
            //자바 스크립트는 함수가 하나의 객체 주소값을 가질 수 있다 자바는 객체만 가능하니 마치 그렇게 보이는 식으로 적은 코드
        }
    }
}
