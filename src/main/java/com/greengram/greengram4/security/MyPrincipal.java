package com.greengram.greengram4.security;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor //빈등록 시 잇어야 오류가 안남
@AllArgsConstructor
public class MyPrincipal {
    private int iuser;
}
