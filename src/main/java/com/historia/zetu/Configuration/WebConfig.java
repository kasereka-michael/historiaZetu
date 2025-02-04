package com.historia.zetu.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Apply caching for images located in multiple directories
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/**", "classpath:/static/assets/imgs/**","classpath:/static/img/placeholder/**","classpath:/static/assets/img/**" ,"classpath:/static/blog/images/**")
                .setCachePeriod(365 * 24 * 60 * 60)  // Cache for 1 year
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic());

        // If you have other static resources (like CSS or JS), you can apply caching here as well:
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/blog/css/**")
                .setCachePeriod(365 * 24 * 60 * 60)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic());

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/blog/js/")
                .setCachePeriod(365 * 24 * 60 * 60)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic());
    }
}