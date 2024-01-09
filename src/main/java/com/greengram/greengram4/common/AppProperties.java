package com.greengram.greengram4.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app")//yaml에 있는 app 끌어오기 prefix앞에 app 붙음
public class AppProperties {

    private final Jwt jwt = new Jwt();// 이너 클래스 사용 시 해당 클래스를 밖에 클래스에서 객체화를 해주어야한다.
    @Getter
    @Setter
    public class Jwt{//클래스 안 클래스 == 이너 클래스
        private String secret;
        private String headerSchemeName;
        private String tokenType;
        private long accessTokenExpiry;
        private long refreshTokenExpiry;
    }
}
