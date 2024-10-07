package com.br.product_api.config.security.middleware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class middlewareConfig implements WebMvcConfigurer {

    @Bean
    public Middleware middleware() {
        return new Middleware();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(middleware());
    }

}
