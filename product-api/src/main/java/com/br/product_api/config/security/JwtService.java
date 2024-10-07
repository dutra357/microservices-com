package com.br.product_api.config.security;

import com.br.product_api.config.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import static io.jsonwebtoken.lang.Objects.isEmpty;

@Service
public class JwtService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    private Claims getClains(String token) {
        var accessToken = extractToken(token);
        try {
            return Jwts
                    .parser()
                    .verifyWith(generateSign())
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid token: " + ex.getMessage());
        }
    }

    public void validateAccessToken(String token) {
        getClains(token);
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new AuthenticationException("The access token was not informed.");
        }

        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }

        return token;
    }

    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(apiSecret.getBytes());
    }
}
