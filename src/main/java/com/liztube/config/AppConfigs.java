package com.liztube.config;

import com.liztube.utils.jackson.HibernateAwareObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by laurent on 06/07/2014.
 */
@EnableWebMvc
@ComponentScan(basePackages = {"com.liztube.controller, com.liztube.service"})
@Configuration
public class AppConfigs extends WebMvcConfigurerAdapter {
    private static final int CACHE_PERIOD = 31556926;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app/dist/css/**").addResourceLocations("/app/dist/css/").setCachePeriod(CACHE_PERIOD);
        registry.addResourceHandler("/app/dist/js/**").addResourceLocations("/app/dist/js/").setCachePeriod(CACHE_PERIOD);
        registry.addResourceHandler("/app/dist/img/**").addResourceLocations("/app/dist/img/").setCachePeriod(CACHE_PERIOD);
        registry.addResourceHandler("/app/dist/libs/**").addResourceLocations("/app/dist/libs/").setCachePeriod(CACHE_PERIOD);
        registry.addResourceHandler("/app/dist/partials/**").addResourceLocations("/app/dist/partials/").setCachePeriod(CACHE_PERIOD);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void configureMessageConverters (List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(new HibernateAwareObjectMapper());
        converters.add(mappingJackson2HttpMessageConverter);

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(stringHttpMessageConverter);

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        byteArrayHttpMessageConverter.supports(MediaType.IMAGE_PNG.getClass());
        converters.add(byteArrayHttpMessageConverter);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(524288000);
        //multipartResolver.setUploadTempDir();
        return multipartResolver;
    }
}
