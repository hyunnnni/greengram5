package com.greengram.greengram4.security.oauth2.userinfo;

import com.greengram.greengram4.security.oauth2.SocialProviderType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OAuth2UserInfoFactory {//객체 생성 하는 공장 팩토리라고 주로 네이밍 함
    public OAuth2UserInfo getOAuth2UserInfo (SocialProviderType socialProviderType,//타입에 따라 네이버인지 카카오인지 체크
                                            Map<String, Object> attrs){

        return switch (socialProviderType){
            case KAKAO -> new KakaoOAuth2UserInfo(attrs);
            case NAVER -> new NaverOAuth2UserInfo(attrs);
            default -> throw new IllegalArgumentException("Invalid Provider Type.");
        };
    }
}
