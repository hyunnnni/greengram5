package com.greengram.greengram4.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor//생성자로 넣어준다
public enum AuthErrorCode implements ErrorCode{//enum : Const를 대체할 수 있는

    NOT_EXIST_USER_ID(HttpStatus.NOT_FOUND,"아이디가 존재하지 않습니다."),
    VALID_PASSWORD(HttpStatus.NOT_FOUND,"비밀번호를 확인해주세요"),
    NEED_SIGNIN(HttpStatus.UNAUTHORIZED,"로그인이 필요합니다"),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND,//NOT_FOUND 상수 객체화를 해서 AuthErrorCode주소값을 NOT_FOUND_REFRESH_TOKEN에 담겠다가 enum
            "refresh_token이 없습니다");

    private final HttpStatus httpStatus;
    private final String message;

/*    AuthErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message
    } 이 작업으로 값을 넣어주고 있다*/
}
