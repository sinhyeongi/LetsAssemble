package com.la.letsassemble.config;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Role.UsersRole;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.PartyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final PartyInfoService partyInfoService;
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 구독을 위한 브로커 설정
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트에서 메세지를 보낼 때 사용하는 URL 접두사 설정
    }

    @Override //
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:0880")
                .withSockJS()
                .setInterceptors(httpSessionHandshakeInterceptor());
    }

    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor(){
        return new HandshakeInterceptor() {
            @Override // 연결전 수행
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if(auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof PricipalDetails)){
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    response.getHeaders().add("FORBIDDEN",HttpStatus.FORBIDDEN.toString());
                    return false;
                }
                Users u = ((PricipalDetails) auth.getPrincipal()).getUser();

                MultiValueMap<String,String> queryParams = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
                Long partyId = Long.parseLong(queryParams.getFirst("partyId"));
                if(u.getRole().toString().equals(UsersRole.ROLE_ADMIN.toString())|| partyInfoService.findByPartIdAndUser(partyId,u.getEmail()).isPresent()){
                    attributes.put("partyId",partyId);
                    return true;
                }
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return false;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

            }
        };
    }


}
