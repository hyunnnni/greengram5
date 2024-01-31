package com.greengram.greengram4.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app")//yaml에 있는 app 끌어오기 prefix앞에 app 붙음
//Greengram4Application에 @ConfigurationPropertiesScan를 붙여야 이 애노테이션 사용이 가능함
public class AppProperties {

    private final Jwt jwt = new Jwt();// 이너 클래스 사용 시 해당 클래스를 밖에 클래스에서 객체화를 해주어야한다.
    @Getter
    @Setter
    public class Jwt{//클래스 안 클래스 == 이너 클래스
        //static으로 만드는 게 좋다
        //객체화 시 속성에 접근이 가능해짐 메모리 릭?현상이 일어남 접근을 할 수 없게 해주어야 한다
        //이 클래스는 yaml에 있는 app 밑 jwt를 지칭하고
        //멤버필드는 jwt밑을 지칭하는 것
        //그래서 yaml에 있는 값들이 setter를 통해서 주입이 된다
        private String secret;
        private String headerSchemeName;
        private String tokenType;
        private long accessTokenExpiry;
        private long refreshTokenExpiry;
        private int refreshTokenCookieMaxAge;//setter로 값이 계산되어 주입
        //쿠키는 초 값으로 넣어야하기에 /1000 = 1296000 값이 들어감

        public void setRefreshTokenExpiry(long refreshTokenExpiry){
            //내가 작성한 게 setter보다 우선순위가 높아서 이 값만 바로 들어가는 게 아닌 여길 통해서
            //멤버필드에 주입

            this.refreshTokenExpiry = refreshTokenExpiry;
            this.refreshTokenCookieMaxAge = (int)refreshTokenExpiry / 1000;
        }
    }
}
