package com.greengram.greengram4.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Slf4j
@Configuration//새로고침 or 주소창 그대로 엔터 시 에러
//고객이 요청 시 dispatcherServlet에서 어디에 배치를 할 지 핸들러매핑에 물어본 후 컨트롤러에 알려줌
//만약 매핑되는 곳이 없다면 static에서 찾아본다 index.html파일이 열리면서 리액트가 동작된다
//그래서 주소에 /를 치면 index.html파일을 찾아보고 주소 엔터 시 리액트가 동작되며 해당 페이지의 주소값이 자동으로 완성된다.?
//그렇기 때문에 새로고침 시에 오류가 안 나게 하려면 static파일 안에 해당되는 부분의 파일을 만든 후
//그 안에 index.html을 넣어준다
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/**")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        // If we actually hit a file, serve that. This is stuff like .js and .css files.
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }
                        // Anything else returns the index.
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }
}