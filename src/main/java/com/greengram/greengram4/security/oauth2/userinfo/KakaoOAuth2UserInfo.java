package com.greengram.greengram4.security.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }


    @Override
    public String getId() {//넘어오는 json을 해쉬맵으로 원하는 타입으로 변경 된 걸 리턴해주는 것
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {//닉네임은 해쉬맵 형태로 properties로 있다 그리고 그 안에 또 해쉬맵으로 되어 있는 걸 꺼내오는 것
        Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");
        return properties == null ? null : (String)properties.get("nickname");
    }

    @Override
    public String getEmail() {

        return (String) attributes.get("account_email");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");
        return properties == null ? null : (String)properties.get("thumbnail_image");
    }
}
