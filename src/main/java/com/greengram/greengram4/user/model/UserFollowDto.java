package com.greengram.greengram4.user.model;

import lombok.Data;

@Data
public class UserFollowDto {
    private Long fromIuser;
    private Long toIuser;
}
