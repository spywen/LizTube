package com.liztube.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@Configuration
@PropertySource("classpath:application.properties")
public class PropertiesConfigs {
    @Bean
    public static PropertySourcesPlaceholderConfigurer setPropertyFile() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
