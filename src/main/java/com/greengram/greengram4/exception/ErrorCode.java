package com.greengram.greengram4.exception;


import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();//enum이 갖고 있는 메소드라 enum을 쓰면 따로 구현부를 만들지 않아도 된다
    HttpStatus getHttpStatus();//getter가 있으면 구현부를 만들지 않아도 된다
    String getMessage();//getter가 있으면 구현부를 만들지 않아도 된다
}
