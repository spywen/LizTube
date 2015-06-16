package com.liztube.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Created by laurent on 19/07/2014.
 */
@Configuration
@ComponentScan(value = "com.liztube.config",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = RootConfigs.class),
        })
public class RootConfigs {
}
