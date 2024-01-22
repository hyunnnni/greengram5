package com.greengram.greengram4.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{//예외를 잡아주는 controller
    //AOP : 로그 찍을 때 또는 예외처리, 트랜잭션에 많이 쓰는 편
    // 관점지향 프로그래밍(중복되는 코드들을 정리?해주는 방식 :단순 메소드 호출이 아닌 마치 그 코드가 적힌 거 처럼 그 부분이 실행이 된다 )
    //filter: 튕겨내야할 때 /로그인 등등
    //interceptor :
    //상속을 받지 않고 예외처리를 하려면 두 곳에서 예외처리가 진행되기 때문에 관리하기 어려워
    //상속을 꼭 받아야 한다

/*    @ExceptionHandler(IllegalCallerException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e){
        log.warn("HandleIllegalArgument", e);
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER);
    }valid 에러를 표시하기 위해 오버라이딩*/

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e){
        log.warn("handleException", e);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RestApiException.class)//서버 메세지 외에 내가 설정한 메세지를 보내고 싶을 때 사용되는 메소드
    public ResponseEntity<Object> handlerestApiException(RestApiException e){
        log.warn("handlerestApiException", e);
        return handleExceptionInternal(e.getErrorCode());
    }

    /*@ExceptionHandler(MethodArgumentNotValidException.class)//Valid를 사용한 부분에서 정해놓은 거 외에 값이 들어와 에러가 발생했을 때의 메소드
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.warn("handleMethodArgumentNotValidException", e);
       *//* List<String> errors = new ArrayList<>();
        for(FieldError lfe : e.getBindingResult().getFieldErrors()){//fieldError라는 리스트를 반복문으로 나눠
            //여기서 오는 getFieldErrors는 valid에 걸려진 애노테이션의 에러메세지가 담겨온다
            errors.add(lfe.getDefaultMessage());//디폴트 메세지를 String 리스트에 add
        }*//*
        List<String> errors = e.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(lfe -> lfe.getDefaultMessage())
                                .collect(Collectors.toList());
        //stream 물레방아처럼 한땀한땀 처리? 일회용임 데이터를 흐르게 함 마무리가 되면 리소스가 해제되어 또 쓰고 싶다면 또 적어주어야 한다.리스트만 가능
        //map 앞 리스트와 사이즈가 똑같은 리스트를 만들어 리턴해준다 근데 타입도 대입연사자에 맞춰 변경해줌 값이 들어오면 리턴을 꼭 해주어야 함
        // filter라는 메소드도 있는데 사이즈가 달라질 수 있음
        //배열은 어렵긔 리스트로 변환 후 stream으로 만들 수 있음
        //크기는 갖고 내용물을 조금 다르게 바꾸고싶을 때 쓰는 메소드
        //collect : Stream<String> 상태였는데 콜렉션으로 List<String>으로 변화됨

        String errStr = "["+String.join( ", " , errors)+"]";
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, errors.toString());
    } 상속을 받아야 하기에 오버라이딩으로 진행해야됨 */

    // @Valid 어노테이션으로 넘어오는 에러 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, CommonErrorCode.INVALID_PARAMETER);

    }
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return handleExceptionInternal(errorCode, null);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode
            , String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(message == null
                        ? makeErrorResponse(errorCode)
                        : makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return makeErrorResponse(errorCode, errorCode.getMessage());
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        //메세지를 유연하게 바꿀 수 있는 메소드
        //에러 발생 시 코드와 메세지를 응답하기 위한 메소드
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

    // @Valid 어노테이션으로 넘어오는 에러 처리 메세지를 보내기 위한 메소드
    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    // 코드 가독성을 위해 에러 처리 메세지를 만드는 메소드 분리
    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .valid(validationErrorList)
                .build();
    }


}
