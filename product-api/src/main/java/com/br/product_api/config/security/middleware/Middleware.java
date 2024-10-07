package com.br.product_api.config.security.middleware;

import com.br.product_api.config.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class Middleware implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    private static final String AUTHORIZATION = "Authorization";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isOptions(request)) {
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
