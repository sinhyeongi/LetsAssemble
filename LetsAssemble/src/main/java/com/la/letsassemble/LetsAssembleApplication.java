package com.la.letsassemble;

import com.la.letsassemble.Util.InicisUtil;
import com.la.letsassemble.config.InicisConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class LetsAssembleApplication {
    @PersistenceContext
    private EntityManager em;
    @Bean
    BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }
    @Bean
    InicisUtil inicisUtil(InicisConfig config) {
        return new InicisUtil(config);
    }
    public static void main(String[] args) {
        SpringApplication.run(LetsAssembleApplication.class, args);
    }
}
