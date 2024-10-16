package com.br.product_api.config.security.middleware;

import com.br.product_api.config.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class Middleware implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtService jwtService;
    public Middleware(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isOptions(request)) {
            return true;
        }

        var authorization = request.getHeader(AUTHORIZATION);
        jwtService.validateAccessToken(authorization);

        return true;
    }

    private boolean isOptions(HttpServletRequest request) {
        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
