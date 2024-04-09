package com.la.letsassemble.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
public class InicisConfig {
    @Value("${portone.apikey}")
    private String apiKey;
    @Value("${portone.secret}")
    private String apisecret;
}
