package com.br.product_api.config.security.dto;

import io.jsonwebtoken.Claims;

import java.util.Objects;

public class JwtResponse {

    private Integer id;
    private String name;
    private String email;

    public static JwtResponse getUser(Claims jwtClaims) {
        try {
            return new JwtResponse((Integer) jwtClaims.get("id"),
                    (String) jwtClaims.get("name"),
                    (String) jwtClaims.get("email"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JwtResponse() {}
    public JwtResponse(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtResponse that = (JwtResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
