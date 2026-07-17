package com.example.gchc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic());

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic());

        registry.addResourceHandler("/favicon.ico", "/favicon.png", "/favicon-color.png")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String adminIndex = "forward:/admin/index.html";

        registry.addViewController("/admin").setViewName(adminIndex);
        registry.addViewController("/admin/").setViewName(adminIndex);
        registry.addViewController("/admin/login").setViewName(adminIndex);
        registry.addViewController("/admin/home").setViewName(adminIndex);
        registry.addViewController("/admin/about").setViewName(adminIndex);
        registry.addViewController("/admin/products").setViewName(adminIndex);
        registry.addViewController("/admin/news").setViewName(adminIndex);
        registry.addViewController("/admin/articles").setViewName(adminIndex);
        registry.addViewController("/admin/users").setViewName(adminIndex);
        registry.addViewController("/admin/categories").setViewName(adminIndex);
        registry.addViewController("/admin/messages").setViewName(adminIndex);
        registry.addViewController("/admin/contact").setViewName(adminIndex);
        registry.addViewController("/admin/settings/footer").setViewName(adminIndex);
        registry.addViewController("/admin/settings/global").setViewName(adminIndex);

        String siteIndex = "forward:/index.html";
        registry.addViewController("/about").setViewName(siteIndex);
        registry.addViewController("/products").setViewName(siteIndex);
        registry.addViewController("/products/{id}").setViewName(siteIndex);
        registry.addViewController("/news").setViewName(siteIndex);
        registry.addViewController("/news/{id}").setViewName(siteIndex);
        registry.addViewController("/contact").setViewName(siteIndex);
    }
}
