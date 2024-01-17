package com.greengram.greengram4.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {//에러 발생 시 코드와 메세지를 응답해주는 역할
    private final String code;
    private final String message;
}
