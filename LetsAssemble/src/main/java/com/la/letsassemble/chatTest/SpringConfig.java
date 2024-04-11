package com.la.letsassemble.chatTest;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SpringConfig  implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // stomp 접속 url => /ws-stomp
        registry.addEndpoint("ws-steomp") // 연결 될 엔드 포인트
                .withSockJS();  // SocketJS를 연결하는 설정
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //메세지를 구독하는 요청 url => 메세지 받기
        registry.enableSimpleBroker("/sub");
        //메세지를 발행하는 요청 url => 메세지 보내기
        registry.setApplicationDestinationPrefixes("/pub");

    }
}
