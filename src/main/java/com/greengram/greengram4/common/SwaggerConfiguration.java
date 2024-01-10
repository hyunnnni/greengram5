package com.greengram.greengram4.common;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Greengram Ver.4"
        , description = "인스타그램 클론 코딩 v4"
        , version = "4.0.0"
        ),
        security = @SecurityRequirement(name = "authorization")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP
        , name = "authorization"//오타가 있으면 안됨
        , in = SecuritySchemeIn.HEADER
        , bearerFormat = "JWT"
        , scheme = "Bearer"
)//로그인 했을 때 안 했을 때 처리를 스웨거에서도 할 수 있게 된다
public class SwaggerConfiguration {
/*    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Greengram Ver.3")
                        .description("인스타그램 클론 코딩 v3")
                        .version("2.0.0"));
    } 이걸 애노테이션으로 작성하면 훨씬 편하다 */
}

