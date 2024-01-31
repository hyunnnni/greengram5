package com.greengram.greengram4.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T> {//응답시 코드와 메세지도 같이 묶어서 보낼 수 있다
    private final String path;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final T data;
}