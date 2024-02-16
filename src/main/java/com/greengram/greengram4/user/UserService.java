package com.greengram.greengram4.user;

import com.greengram.greengram4.common.*;
import com.greengram.greengram4.entity.UserEntity;
import com.greengram.greengram4.entity.UserFollowEntity;
import com.greengram.greengram4.entity.UserFollowIds;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final UserRepository repository;
    private final UserFollowRepository followRepository;

    public ResVo postsignup(UserInsSignupDto dto){//jpa를 사용한 insert
        UserEntity entity = UserEntity.builder()
                .providerType(ProviderTypeEnum.LOCAL)
                .uid(dto.getUid())
                .upw(passwordEncoder.encode(dto.getUpw()))
                .nm(dto.getNm())
                .pic(dto.getPic())
                .role(RoleEunm.USER)
                .build();

        repository.save(entity);
        return new ResVo(entity.getIuser().intValue());
    }
    /*public ResVo postsignup(UserInsSignupDto dto){
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
    }*/

    public UserSigninVo postsignin (HttpServletResponse res, UserSigninDto dto) {
        Optional<UserEntity> optionalUserEntity =
                repository.findByProviderTypeAndUid(ProviderTypeEnum.LOCAL, dto.getUid());
        UserEntity entity = optionalUserEntity.orElseThrow(
                () -> new RestApiException(AuthErrorCode.NOT_EXIST_USER_ID));

        if(!passwordEncoder.matches(dto.getUpw(), entity.getUpw())){
            throw new RestApiException(AuthErrorCode.VALID_PASSWORD);
        }
        int iuser = entity.getIuser().intValue();
        MyPrincipal myPrincipal = MyPrincipal.builder()
                .iuser(iuser)
                .build();
        myPrincipal.getRoles().add(entity.getRole().name());


        String at = jwtTokenProvider.generateAccessToken(myPrincipal);
        String rt = jwtTokenProvider.generateRefreshToken(myPrincipal);

        //rt > cookie에 담을꺼임
        int rtCookieMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "rt");
        cookieUtils.setCookie(res, "rt", rt, rtCookieMaxAge);

        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .iuser(iuser)
                .nm(entity.getNm())
                .pic(entity.getPic())
                .firebaseToken(entity.getFirebaseToken())
                .accessToken(at)
                .build();

    }

   /* public UserSigninVo postsignin (HttpServletResponse res, UserSigninDto dto){

        UserSigninDto sDto = UserSigninDto.builder()
                .uid(dto.getUid())
                .build();

        UserModel entity = mapper.selUser(dto);

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

*//*        HttpSession session = req.getSession(true);
        session.setAttribute("loginUserPk", entity.getIuser()); 세션 테스트 *//*

        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .iuser(entity.getIuser())
                .nm(entity.getNm())
                .pic(entity.getPic())
                .accessToken(asscessToken)
                .firebaseToken(entity.getFirebaseToken())
                .build();

    }*/

    public ResVo signout(HttpServletResponse res){
        cookieUtils.deleteCookie(res, "rt");
        return new ResVo(1);
    }

    public UserSigninVo getRefreshToken(HttpServletRequest req){//액세스 토큰을 다시 만들어서 다시 주는 작업
        //리프레시 토큰에 비해 액세스 토큰은 탈취 당할 경우가 있기에 만료기간이 짧다 그래서 만료 시 다시 만들어주는 작업이 필요
        //자동로그인 원리
        //Cookie cookie = cookieUtils.getCookie(req,"rt");

        Optional<String> optRt = cookieUtils.getCookie(req,"rt").map(Cookie::getValue);


        if(optRt.isEmpty()){
            return UserSigninVo.builder()
                    .result(Const.FAIL)
                    .accessToken(null)
                    .build();
        }

        String token = optRt.get();

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
        UserFollowIds ids= new UserFollowIds();
        ids.setFromIuser((long)authenticationFacade.getLoginUserPk());
        ids.setToIuser(dto.getToIuser());

        AtomicInteger atomic = new AtomicInteger(Const.FAIL);

        followRepository
                .findById(ids)
                .ifPresentOrElse(
                        entity -> {followRepository.delete(entity);}//존재한다면 삭제
                        ,() -> {//존재하지 않는다면
                            atomic.set(Const.SUCCESS);
                            UserFollowEntity saveUserFollowEntity = new UserFollowEntity();
                            saveUserFollowEntity.setUserFollowIds(ids);
                            UserEntity fromUserEntity = repository.getReferenceById((long)authenticationFacade.getLoginUserPk());
                            UserEntity toUserEntity = repository.getReferenceById(dto.getToIuser());
                            saveUserFollowEntity.setFromUserEntity(fromUserEntity);
                            saveUserFollowEntity.setToUserEntity(toUserEntity);
                            followRepository.save(saveUserFollowEntity);
                        }
                );
        return new ResVo(atomic.get());
    }

/*    public ResVo toggleFollow(UserFollowDto dto){
        int result = mapper.delFollow(dto);
        if(result == 1){
            return new ResVo(Const.FAIL);
        }
        result = mapper.insFollow(dto);
        return new ResVo(Const.SUCCESS);
    }*/

    public UserInfoVo getUserInfoSel(UserInfoSelDto dto){
        return mapper.userInfoSel(dto);
    }

    @Transactional
    public ResVo patchUserFirebaseToken(UserFirebaseTokenPatchDto dto) {
        UserEntity entity = repository.getReferenceById((long)authenticationFacade.getLoginUserPk());
        entity.setFirebaseToken(dto.getFirebaseToken());
        return new ResVo(Const.SUCCESS);
    }

    /*public ResVo patchUserFirebaseToken(UserFirebaseTokenPatchDto dto) {
        int affectedRows = mapper.updUserFirebaseToken(dto);
        return new ResVo(affectedRows);
    }*/

    @Transactional
    public UserPicPatchDto patchUserPic(MultipartFile pic) {
        Long iuser = Long.valueOf(authenticationFacade.getLoginUserPk());
        UserEntity entity = repository.getReferenceById(iuser);
        String path = "/user/" + iuser;
        mfu.delFolderTrigger(path);
        String savedPicFileNm = mfu.transferTo(pic, path);
        entity.setPic(savedPicFileNm);

        UserPicPatchDto dto = new UserPicPatchDto();
        dto.setIuser(iuser.intValue());
        dto.setPic(savedPicFileNm);
        return dto;


    }
/*    public UserPicPatchDto patchUserPic(MultipartFile pic) {
        UserPicPatchDto dto = new UserPicPatchDto();
        dto.setIuser(authenticationFacade.getLoginUserPk());
        String path = "/user/"+dto.getIuser();
        mfu.delFolderTrigger(path);
        String savedPicFileNm = mfu.transferTo(pic, path);
        dto.setPic(savedPicFileNm);
        int affectedRows = mapper.updUserPic(dto);
        return dto;
    }*/
}
