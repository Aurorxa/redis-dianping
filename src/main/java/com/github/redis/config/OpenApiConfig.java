package com.github.redis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 15:25:45
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Redis 学习")
                        .description("Redis 学习")
                        .version("0.0.1"));
    }

}