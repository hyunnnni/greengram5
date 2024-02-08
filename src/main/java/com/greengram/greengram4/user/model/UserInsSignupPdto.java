package com.greengram.greengram4.user.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserInsSignupPdto {
    private int iuser;
    private String providerType;
    private String uid;
    private String upw;
    private String nm;
    private String pic;
    private String role;
}
