package com.greengram.greengram4.user.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserPicPatchDto {
    private int iuser;//로그인 토큰이 없을 때 필요했던 인증수단
    private String pic;//멀티파트파일로 받을거임
}
