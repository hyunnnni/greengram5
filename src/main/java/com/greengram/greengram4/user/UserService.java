package com.greengram.greengram4.user;

import com.greengram.greengram4.common.*;
import com.greengram.greengram4.exception.AuthErrorCode;
import com.greengram.greengram4.exception.RestApiException;
import com.greengram.greengram4.security.AuthenticationFacade;
import com.greengram.greengram4.security.JwtTokenProvider;
import com.greengram.greengram4.security.MyPrincipal;
import com.greengram.greengram4.security.MyUserDetails;
import com.greengram.greengram4.user.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;//SecurityConfiguration에서 빈등록한 것
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final MyFileUtils mfu;
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

    public UserSigninVo postsignin (HttpServletRequest req, HttpServletResponse res, UserSigninDto dto){
        UserSigninDto sDto = UserSigninDto.builder()
                .uid(dto.getUid())
                .build();

        UserSelEntity entity = mapper.selUser(dto);

        if(entity == null){
            //return UserSigninVo.builder().result(Const.LOGIN_NO_UID).build();
            throw new RestApiException(AuthErrorCode.NOT_EXIST_USER_ID);//에러 메세지 쓰로우
        }

        if(!passwordEncoder.matches(dto.getUpw(), entity.getUpw())){

            //return UserSigninVo.builder().result(Const.LOGIN_DIFF_UPW).build();
            throw new RestApiException(AuthErrorCode.VALID_PASSWORD);//에러 메세지 쓰로우

        }

        MyPrincipal mp = MyPrincipal.builder()
                .iuser(entity.getIuser())
                .build();
        mp.getRoles().add(entity.getRole());

        String asscessToken= jwtTokenProvider.generateAccessToken(mp);
        String refreshToken= jwtTokenProvider.generateRefreshToken(mp);

        //rt > cookie에 담는 작업
        int rtCookieMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie( res, "rt");
        cookieUtils.setCookie(res, "rt", refreshToken, rtCookieMaxAge);

/*        HttpSession session = req.getSession(true);
        session.setAttribute("loginUserPk", entity.getIuser()); 세션 테스트 */

        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .iuser(entity.getIuser())
                .nm(entity.getNm())
                .pic(entity.getPic())
                .accessToken(asscessToken)
                .firebaseToken(entity.getFirebaseToken())
                .build();

    }

    public ResVo signout(HttpServletResponse res){
        cookieUtils.deleteCookie(res, "rt");
        return new ResVo(1);
    }

    public UserSigninVo getRefreshToken(HttpServletRequest req){//액세스 토큰을 다시 만들어서 다시 주는 작업
        //리프레시 토큰에 비해 액세스 토큰은 탈취 당할 경우가 있기에 만료기간이 짧다 그래서 만료 시 다시 만들어주는 작업이 필요
        //자동로그인 원리
        Cookie cookie = cookieUtils.getCookie(req,"rt");

        if(cookie == null){
            return UserSigninVo.builder()
                    .result(Const.FAIL)
                    .accessToken(null)
                    .build();
        }

        String token = cookie.getValue();

        if(!jwtTokenProvider.isValidateToken(token)){
            return UserSigninVo.builder()
                    .result(Const.FAIL)
                    .accessToken(null)
                    .build();
        }

        MyUserDetails myUserDetails = (MyUserDetails) jwtTokenProvider.getUserDetailsFromToken(token);
        MyPrincipal myPrincipal = myUserDetails.getMyPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(myPrincipal);



        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .accessToken(accessToken)
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

    public UserPicPatchDto patchUserPic(MultipartFile pic) {
        UserPicPatchDto dto = new UserPicPatchDto();
        dto.setIuser(authenticationFacade.getLoginUserPk());
        String path = "/user/"+dto.getIuser();
        mfu.delFolderTrigger(path);
        String savedPicFileNm = mfu.transferTo(pic, path);
        dto.setPic(savedPicFileNm);
        int affectedRows = mapper.updUserPic(dto);
        return dto;
    }
}
