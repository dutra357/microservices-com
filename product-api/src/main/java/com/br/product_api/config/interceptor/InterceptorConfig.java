package com.br.product_api.config.interceptor;

import com.br.product_api.config.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(jwtService());
    }

    public void addInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor((authInterceptor()));
    }
}
