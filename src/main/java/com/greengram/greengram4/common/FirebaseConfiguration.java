package com.greengram.greengram4.common;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Slf4j
@Configuration//설정할 때 쓰는 빈등록 축하합니달라
public class FirebaseConfiguration {
    @Value("${fcm.certification}")
    private String googleApplicationCredentials; // = serviceAccountKey.json 이런 느낌~
    //final로 하고 생성자로 넣는게 더 좋음
    //yaml에서 fcm아래 certification값이 들어온다

    @PostConstruct//생성자 다음으로 호출됨 근데 딱 한 번(어차피 생성자가 한 번만 호출됨)
    //쓰는 이유 딱 한 번만 실행되길 원할 때 DI가 되고 난 후에 호출되었으면 할 때
    public void init() {
        try {
            InputStream serviceAccount =
                    new ClassPathResource(googleApplicationCredentials).getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                log.info("FirebaseApp Initialization Complete !!!");
                FirebaseApp.initializeApp(options);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
