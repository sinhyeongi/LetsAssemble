package com.la.letsassemble.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return null;
    }
    @Bean
    AuthenticationFailureHandler customAuthFailureHandler(){
        return null;
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity security) throws Exception{
        security.csrf(AbstractHttpConfigurer :: disable);
        security.authorizeHttpRequests(auth ->{
                auth
                        .requestMatchers("/error/**").denyAll()
                        .requestMatchers("/mamnager/**").hasAnyRole("MANAGER","ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll();
        });
           /*     .formLogin(
               form ->{
                   form.loginPage("/loginForm")
                           .loginProcessingUrl("/login")
                           .failureHandler(null)
                           .successHandler(
                                   new AuthenticationSuccessHandler() {
                                       @Override
                                       public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                           response.sendRedirect("/");
                                       }
                                   }
                           );
               }
        );
        */

        return security.build();
    }
}
