package com.greengram.greengram4.security.oauth2.userinfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo{

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }


    @Override
    public String getId() {//response라는 객체 안 해쉬맵으로 id가 있다 그걸 가져오는 것
        Map<String, Object> res = (Map<String, Object>)attributes.get("response");
        return res == null ? null : (String)res.get("id");
    }

    @Override
    public String getName() {
        Map<String, Object> res = (Map<String, Object>)attributes.get("response");
        return res == null ? null : (String)res.get("name");
    }

    @Override
    public String getEmail() {
        Map<String, Object> res = (Map<String, Object>)attributes.get("response");
        return res == null ? null : (String)res.get("email");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> res = (Map<String, Object>)attributes.get("response");
        return res == null ? null : (String)res.get("profile_image");
    }
}
