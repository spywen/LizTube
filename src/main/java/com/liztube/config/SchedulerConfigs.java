package com.liztube.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Scheduler configuration
 */
@Configuration
@EnableScheduling
@ComponentScan(basePackages = {"com.liztube.scheduler"})
public class SchedulerConfigs {

}
