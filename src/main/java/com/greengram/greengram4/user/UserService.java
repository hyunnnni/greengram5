package com.greengram.greengram4.user;

import com.greengram.greengram4.common.AppProperties;
import com.greengram.greengram4.common.Const;
import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.security.JwtTokenProvider;
import com.greengram.greengram4.security.MyPrincipal;
import com.greengram.greengram4.user.model.*;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;//SecurityConfiguration에서 빈등록한 것
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    public ResVo postsignup(UserInsSignupDto dto){
        UserInsSignupPdto pdto= UserInsSignupPdto.builder()
                .uid(dto.getUid())
                .upw(passwordEncoder.encode(dto.getUpw()))
                .nm(dto.getNm())
                .pic(dto.getPic())
                .build();
        int result = mapper.userInsSignup(pdto);
        if(result == 0){
            return new ResVo(result);
        }
        return new ResVo(pdto.getIuser());
    }

    public UserSigninVo postsignin (UserSigninDto dto){
        UserSigninDto sDto = new UserSigninDto();
        sDto.setUid(dto.getUid());

        UserSelEntity entity = mapper.selUser(dto);

        if(entity == null){
            return UserSigninVo.builder().result(Const.LOGIN_NO_UID).build();
        }

        if(!passwordEncoder.matches(dto.getUpw(), entity.getUpw())){

            return UserSigninVo.builder().result(Const.LOGIN_DIFF_UPW).build();

        }
        MyPrincipal mp = new MyPrincipal(dto.getIuser());
        String asscessToken= jwtTokenProvider.generateAccessToken(mp);
        String refreshToken= jwtTokenProvider.generateRefreshToken(mp);

        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .iuser(entity.getIuser())
                .nm(entity.getNm())
                .pic(entity.getPic())
                .accessToken(asscessToken)
                .firebaseToken(entity.getFirebaseToken())
                .build();

    }

    public ResVo toggleFollow(UserFollowDto dto){
        int result = mapper.delFollow(dto);
        if(result == 1){
            return new ResVo(Const.FAIL);
        }
        result = mapper.insFollow(dto);
        return new ResVo(Const.SUCCESS);
    }

    public UserInfoVo getUserInfoSel(UserInfoSelDto dto){
        return mapper.userInfoSel(dto);
    }

    public ResVo patchUserFirebaseToken(UserFirebaseTokenPatchDto dto) {
        int affectedRows = mapper.updUserFirebaseToken(dto);
        return new ResVo(affectedRows);
    }

    public ResVo patchUserPic(UserPicPatchDto dto) {
        int affectedRows = mapper.updUserPic(dto);
        return new ResVo(affectedRows);
    }
}
