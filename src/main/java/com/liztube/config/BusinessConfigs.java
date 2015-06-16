package com.liztube.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by laurent on 19/07/2014.
 */
@Configuration
@ComponentScan(value = {"com.liztube.business"})
@EnableTransactionManagement
public class BusinessConfigs {}
