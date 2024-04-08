package com.la.letsassemble.config;

import com.la.letsassemble.Security_Custom.CustomAuthenticationFailure;
import com.la.letsassemble.Security_Custom.PrincipalOauthUserService;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PrincipalOauthUserService principalOauthUserService;

    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> {
            web.ignoring().requestMatchers(new String[]{"/favicon.ico","/resources/**","/error"});
        });

    }

    @Bean
    AuthenticationFailureHandler customAuthFailurHandler(){
        return new CustomAuthenticationFailure();
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity security) throws Exception{
        security.csrf(AbstractHttpConfigurer :: disable);
        security.authorizeHttpRequests(auth ->{
                auth
                        .requestMatchers("/oauth2/login").authenticated()
                        .requestMatchers("/error/**").denyAll()
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll();
        }).formLogin(
                form ->{
                   form.loginPage("/loginForm")
                           .loginProcessingUrl("/login")
                           .usernameParameter("username")
                           .passwordParameter("password")
                           .failureHandler( customAuthFailurHandler())
                           .successHandler(
                                   new AuthenticationSuccessHandler() {
                                       @Override
                                       public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                           response.sendRedirect("/");
                                       }
                                   }
                           );
               }
        ).oauth2Login(
                oauth2 -> oauth2
                        .loginPage("/loginForm")
                        .userInfoEndpoint(userInfoEndpointConfig -> {
                            userInfoEndpointConfig.userService(principalOauthUserService);
                        })

        ).logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // 성공하면 루트 페이지로 이동
                .logoutSuccessUrl("/")
                // 로그아웃 시 생성된 사용자 세션 삭제
                .invalidateHttpSession(true));


        return security.build();
    }
}
