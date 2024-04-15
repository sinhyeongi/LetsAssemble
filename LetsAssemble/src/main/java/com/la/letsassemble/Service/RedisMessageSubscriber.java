package com.la.letsassemble.Service;

import com.la.letsassemble.dto.MessageDTO;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class RedisMessageSubscriber implements MessageListener {
    private final RedisTemplate<String,Object> redisTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    public RedisMessageSubscriber(RedisTemplate<String,Object> redisTemplate,SimpMessagingTemplate simpMessagingTemplate) {
        this.redisTemplate = redisTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String content = new String(message.getBody());
        simpMessagingTemplate.convertAndSend(channel,content);
    }

    public void sendMessageToRedis(String channel, MessageDTO messageDTO){
        String messageJson = new Gson().toJson(messageDTO);
        redisTemplate.convertAndSend(channel,messageJson);
    }
}
