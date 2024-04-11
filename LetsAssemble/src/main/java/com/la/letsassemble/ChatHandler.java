package com.la.letsassemble;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.la.letsassemble.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Component
@Log4j2
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    private static List<WebSocketSession> list = new ArrayList<>();
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = message.getPayload();
        log.info("payload {} ", payload);

        for(WebSocketSession sess : list){
            sess.sendMessage(message);
        }
    }

    /* 클라이언트가 접속시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        list.add(session);

        log.info(session + "클라이언트 접속");
    }

    /* 클라이언트가 접속 해제시 호출되는 메서드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{

        log.info(session + "클라이언트 접속 해제");
        list.remove(session);
    }

}
