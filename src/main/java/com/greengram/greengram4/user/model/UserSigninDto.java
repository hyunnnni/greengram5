package com.greengram.greengram4.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSigninDto {
    private String providerType;
    private String uid;
    private String upw;
    @JsonIgnore
    private int iuser;
}
