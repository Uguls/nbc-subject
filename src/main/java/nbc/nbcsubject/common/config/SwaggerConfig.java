package nbc.nbcsubject.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI openAPI() {

            final String securityScheme = "BearerAuth";

            Info info = new Info()
                    .title("백엔드 개발 과제 (JAVA) Swagger")
                    .description("백엔드 개발 과제 (JAVA)")
                    .version("1.0.0");

            SecurityRequirement securityRequirement = new SecurityRequirement()
                    .addList(securityScheme);

            Components components = new Components()
                    .addSecuritySchemes(securityScheme, new SecurityScheme()
                            .name(securityScheme)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT"));

            return new OpenAPI()
                    .info(info)
                    .addSecurityItem(securityRequirement)
                    .components(components);
        }
}
