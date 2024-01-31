package com.greengram.greengram4.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {//에러 발생 시 코드와 메세지를 응답해주는 역할
    private final String code;
    private final String message;

    // Errors가 없다면 응답이 내려가지 않게 처리
    //valids가 null이 아니고 size가 1이상이라면 JSON으로 만들어진다 아니라면 안 만들어진다.
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> valid;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {// 이너 클래스는 static을 붙여야 한다
        //만약 붙이지 않는다면 이 클래스를 죽일 때 errorResponse 까지 죽여야 하기 때문이다
        // @Valid 로 에러가 들어왔을 때, 어느 필드에서 에러가 발생했는 지에 대한 응답 처리
        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
