package com.greengram.greengram4.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSigninVo {
    private int result;
    private int iuser;
    private String nm;
    private String pic;
}

