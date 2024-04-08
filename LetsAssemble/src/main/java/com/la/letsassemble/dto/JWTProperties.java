package com.la.letsassemble.dto;

public interface JWTProperties {
    String SECRET = "Les";
    int EXPIRATION_TIME = 60000*10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
