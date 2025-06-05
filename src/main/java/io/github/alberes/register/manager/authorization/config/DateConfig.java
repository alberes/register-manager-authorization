package io.github.alberes.register.manager.authorization.config;

import io.github.alberes.register.manager.authorization.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateConfig {

    @Bean
    public DateTimeFormatter dateTimeFormatter(){
        return DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMATTER_PATTERN);
    }
}
