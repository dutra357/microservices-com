package com.br.product_api.config.security.middleware;

import com.br.product_api.config.exception.ValidationException;
import com.br.product_api.config.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static io.jsonwebtoken.lang.Objects.isEmpty;

public class Middleware implements HandlerInterceptor {

    private final JwtService jwtService;
    public Middleware(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        if (isOptions(request) || isPublicUrl(request.getRequestURI())) {
            return true;
        }
        if (isEmpty(request.getHeader(TRANSACTION_ID))) {
            throw new ValidationException("The transactionId header is required.");
        }

        var authorization = request.getHeader(AUTHORIZATION);
        jwtService.validateAccessToken(authorization);
        request.setAttribute("serviceid", UUID.randomUUID().toString());
        return true;
    }

    private boolean isPublicUrl(String url) {
        return Urls.PROTECT_URLS
                .stream()
                .noneMatch(url::contains);
    }

    private boolean isOptions(HttpServletRequest request) {
        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
