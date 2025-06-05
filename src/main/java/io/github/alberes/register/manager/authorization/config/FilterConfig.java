package io.github.alberes.register.manager.authorization.config;

import io.github.alberes.register.manager.authorization.filters.JwtFilter;
import io.github.alberes.register.manager.authorization.services.JWTService;
import io.github.alberes.register.manager.authorization.utils.ControllerUtils;
import io.github.alberes.register.manager.authorization.utils.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JWTService jwtService;

    private final ApplicationContext applicationContext;

    private final EncryptUtils encryptUtils;

    private final ControllerUtils controllerUtils;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterFilterRegistration(){
        FilterRegistrationBean<JwtFilter> jwtFilter =
                new FilterRegistrationBean<JwtFilter>();
        jwtFilter.setFilter(this.jwtFilter());
        jwtFilter.addUrlPatterns("/api/v1/users/*", "/api/v1/users/{userId}/address/*");
        return jwtFilter;
    }

    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter(this.jwtService, this.applicationContext, this.encryptUtils, this.controllerUtils);
    }
/*
    @Bean
    public FilterRegistrationBean<UserAccountValidationFilter> userAccountValidationFilter(){
        FilterRegistrationBean<UserAccountValidationFilter> userAccountValidationFilter =
                new FilterRegistrationBean<UserAccountValidationFilter>();
        userAccountValidationFilter.setFilter(new UserAccountValidationFilter());
        userAccountValidationFilter.addUrlPatterns("/api/v1/users/*", "/api/v1/users/{userId}/address/*");
        return userAccountValidationFilter;
    }
 */
}
