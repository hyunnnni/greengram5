package com.greengram.greengram4.user;

import com.greengram.greengram4.user.model.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int userInsSignup(UserInsSignupPdto pdto);

    int delFollow(UserFollowDto dto);

    int insFollow(UserFollowDto dto);

    UserInfoVo userInfoSel (UserInfoSelDto dto);

    UserSelEntity selUser(UserSigninDto dto);

    int updUserFirebaseToken(UserFirebaseTokenPatchDto dto);
    int updUserPic(UserPicPatchDto dto);

}
