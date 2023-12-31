package com.madeg.logistics.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Logistics API", description = "제품,매장 관리를 위한 인터페이스 규격서", version = "1.0"))
@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi chatOpenApi() {
    String[] paths = { "/" };

    return GroupedOpenApi
        .builder()
        .group("logistics")
        .pathsToMatch(paths)
        .build();
  }

  @Bean
  public OpenAPI api() {
    SecurityScheme apiKey = new SecurityScheme()
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER)
        .name("Authorization");

    SecurityRequirement securityRequirement = new SecurityRequirement()
        .addList("Bearer Token");

    return new OpenAPI()
        .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
        .addSecurityItem(securityRequirement);
  }
}
