package com.greengram.greengram4.security.oauth2.userinfo;

import com.greengram.greengram4.security.oauth2.SocialProviderType;
import com.greengram.greengram4.user.UserMapper;
import com.greengram.greengram4.user.model.UserInsSignupPdto;
import com.greengram.greengram4.user.model.UserSelEntity;
import com.greengram.greengram4.user.model.UserSigninDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomeOAuth2UserService extends DefaultOAuth2UserService {
    private final UserMapper mapper;
    private final OAuth2UserInfoFactory factory;

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user){
        SocialProviderType socialProviderType =
                SocialProviderType.valueOf(userRequest.getClientRegistration()
                                                        .getRegistrationId()
                                                        .toUpperCase());
        //yaml에 있는 id를 대문자로 변경해준 후 socialproviderType로 변경해주는 것

        OAuth2UserInfo OAuth2UserInfo = factory.getOAuth2UserInfo(socialProviderType, user.getAttributes());//카카오냐 네이버냐에 따라 객체 생성해주기

        UserSigninDto dto = UserSigninDto.builder()
                .providerType(socialProviderType.name())
                .uid(OAuth2UserInfo.getId())
                .build();

        UserSelEntity savedUser = mapper.selUser(dto);//null이 들어오면 회원가입을 안 한 유저

        if(savedUser == null){//회원가입 처리

            //mapper.signupUser(OAuth2UserInfo,socialProviderType);
        }

        return null;
    }

    private UserSelEntity signupUser(OAuth2UserInfo OAuth2UserInfo, SocialProviderType socialProviderType){
        UserInsSignupPdto pdto = UserInsSignupPdto.builder()
                .providerType(socialProviderType.name())
                .uid(OAuth2UserInfo.getId())
                .upw("social")
                .nm(OAuth2UserInfo.getName())
                .pic(OAuth2UserInfo.getImageUrl())
                .role("USER")
                .build();


        int result = mapper.userInsSignup(pdto);

        UserSelEntity entity = new UserSelEntity();
        entity.setIuser(pdto.getIuser());
        entity.setRole(pdto.getRole());
        return entity;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);//해당 메소드의 부모꺼의 loadUser를 실행하겠다는 뜻
        try {
            return this.process(userRequest, user);
        }catch (AuthenticationException e){
            throw e;
        }catch (Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage(),e.getCause());
        }
    }
}
