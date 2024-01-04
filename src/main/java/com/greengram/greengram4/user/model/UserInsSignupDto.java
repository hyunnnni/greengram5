package com.greengram.greengram4.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "회원가입 데이터")
public class UserInsSignupDto {
    @Schema(title= "유저 id")
    private String uid;

    @Schema(title= "유저 pw")
    private String upw;

    @Schema(title= "유저 이름")
    private String nm;

    @Schema(title= "유저 프로필 사진")
    private String pic;
}
