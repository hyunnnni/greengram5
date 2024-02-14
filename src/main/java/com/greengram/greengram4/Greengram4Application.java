package com.greengram.greengram4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableJpaAuditing
public class Greengram4Application {

    public static void main(String[] args) {
        SpringApplication.run(Greengram4Application.class, args);
    }

}
