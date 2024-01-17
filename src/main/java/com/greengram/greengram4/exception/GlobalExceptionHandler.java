package com.greengram.greengram4.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {//예외를 잡아주는 controller
    //AOP : 로그 찍을 때 또는 예외처리, 트랜잭션에 많이 쓰는 편
    // 관점지향 프로그래밍(중복되는 코드들을 정리?해주는 방식 :단순 메소드 호출이 아닌 마치 그 코드가 적힌 거 처럼 그 부분이 실행이 된다 )
    //filter: 튕겨내야할 때 /로그인 등등
    //interceptor :

    @ExceptionHandler(IllegalCallerException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e){
        log.warn("HandleIllegalArgument", e);
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e){
        log.warn("handleException", e);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handlerestApiException(RestApiException e){
        log.warn("handlerestApiException", e);
        return handleExceptionInternal(e.getErrorCode());
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode){
        return handleExceptionInternal(errorCode, null);
    }
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message){
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(message == null
                        ? makeErrorResponse(errorCode)
                        : makeErrorResponse(errorCode, message));
    }
    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
    return makeErrorResponse(errorCode, errorCode.getMessage());
    }
    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message){
        //메세지를 유연하게 바꿀 수 있는 메소드
        //에러 발생 시 코드와 메세지를 응답하기 위한 메소드
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }
}
