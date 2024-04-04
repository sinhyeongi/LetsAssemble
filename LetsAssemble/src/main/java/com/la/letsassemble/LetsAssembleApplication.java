package com.la.letsassemble;

import com.la.letsassemble.Util.MessegeFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LetsAssembleApplication {
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager manager){
        return new JPAQueryFactory(manager);
    }
    public static void main(String[] args) {
        SpringApplication.run(LetsAssembleApplication.class, args);
    }
}
