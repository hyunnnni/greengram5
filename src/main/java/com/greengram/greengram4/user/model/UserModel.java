package com.greengram.greengram4.user.model;

import lombok.Data;

@Data
public class UserModel {
    private int iuser;
    private String nm;
    private String pic;
    private String uid;
    private String upw;
    private String firebaseToken;
    private String role;
    private String createdAt;
    private String updatedAt;
}
