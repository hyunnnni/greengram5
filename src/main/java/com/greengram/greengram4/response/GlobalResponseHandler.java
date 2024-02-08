package com.greengram.greengram4.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greengram.greengram4.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
//@RestControllerAdvice(basePackages = "com.greengram.greengram4")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    //기존 컨트롤러에서 리턴하는 구조들이 다 다른데 큰 틀을 통일하고 싶어 사용
    //컨트롤러에서 리턴해주는 걸 @RestControllerAdvice가 잡는다

    private final ObjectMapper om;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //컨트롤러의 반환값이 객체일 때만 return true
        //만약 전부 true로 리턴되게 작성했을 때 String을 리턴하게 되면 객체를 리턴할 때랑 다르게 다른 방식을 사용하여 제대로 동작을 안 할 수가 있다
        //그래서 객체일 때만 true가 되게 넣었다 String은 그래서 아래 메소드를 안 타고 그냥 리턴되게 한다.
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override//위랑 셋뚜
    public Object beforeBodyWrite(Object body//컨트롤러에서 리턴한 것이 이쪽으로 들어온다
                                , MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse =
                ((ServletServerHttpResponse) response).getServletResponse();

        int status = servletResponse.getStatus();
        HttpStatus resolve = HttpStatus.resolve(status);
        String path = request.getURI().getPath();//이 메소드?기능의 경로
        if(resolve != null) {
            if(resolve.is2xxSuccessful()) {//만약 200대 코드라면 아래가 응답이 되게 하는 부분
                return ApiResponse.builder()
                        .path(path)
                        .data(body)
                        .code("Success")
                        .message("통신 성공")
                        .build();
            } else if(body instanceof ErrorResponse) {//만약 다른 에러코드가 발생한다면
                //아래 errorResponse가 해쉬맵 형식으로 변환이 된다
                // 키 값 : 코드 , value : "~"
                // 키 값 : 메세지, value : "~" 이렇게 두 개가 만들어진다.
                Map<String, Object> map = om.convertValue(body, Map.class);
                map.put("path", path);//그리고 키 값 path : value :"path~~"를 넣어 리턴해준다 리턴이 되면서 자동으로 json으로 변경된다
                return map;
            }
        }
        return body;
    }
}