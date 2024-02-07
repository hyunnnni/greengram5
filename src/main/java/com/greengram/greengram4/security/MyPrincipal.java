package com.greengram.greengram4.security;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor //빈등록 시 잇어야 오류가 안남
@AllArgsConstructor
public class MyPrincipal {
    private int iuser;

    @Builder.Default //빌더패턴을 쓸 때 안 적게되면 기본으로 이 값을 쓰겠다라는 의미
    private List<String> roles = new ArrayList<>();
}
