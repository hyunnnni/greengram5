package com.greengram.greengram4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

import java.awt.print.Pageable;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableJpaAuditing
public class Greengram4Application {

    public static void main(String[] args) {
        SpringApplication.run(Greengram4Application.class, args);
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizer(){//전체 셀렉할 때 페이지 값을 스웨거에서 1부터 시작되게 해주는 작업
        return p -> p.setOneIndexedParameters(true);
        /*return new PageableHandlerMethodArgumentResolverCustomizer() {
            @Override
            public void customize(PageableHandlerMethodArgumentResolver pageableResolver) {
                pageableResolver.setOneIndexedParameters(true);
            }
        };*/
    }

}
