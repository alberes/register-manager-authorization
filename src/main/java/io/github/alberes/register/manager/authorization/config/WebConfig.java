package io.github.alberes.register.manager.authorization.config;

import io.github.alberes.register.manager.authorization.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsWebMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry) {
                corsRegistry.addMapping(Constants.SLASH_ASTERISK_ASTERISK)
                        .allowedOrigins(Constants.ASTERISK)
                        .allowedMethods(Constants.ALLOWED_METHODS)
                        .allowedHeaders(Constants.ASTERISK);
            }
        };
    }
}
