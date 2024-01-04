package com.greengram.greengram4.user.model;

import lombok.Data;

@Data
public class UserFirebaseTokenPatchDto {
    private int iuser;
    private String firebaseToken;
}
