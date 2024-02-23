package com.greengram.greengram4.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "openapi")//Application에도 적어줘야 빨간줄이 안생김 @ConfigurationPropertiesScan
public class OpenApiProperties {

    private final Hospital hospital = new Hospital();

    @Getter
    @Setter
    public class Hospital{
        private String baseUrl;
        private String dataUrl;
        private String serviceKey;
    }
}
