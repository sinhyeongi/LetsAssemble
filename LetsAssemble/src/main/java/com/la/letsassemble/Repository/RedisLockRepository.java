package com.la.letsassemble.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {
    private final RedisTemplate<String,String> redisTemplate;
    public Boolean lock(String key,String value){
        Boolean resul = redisTemplate.opsForValue().setIfAbsent(key,value);
        if(resul != null && resul){
            redisTemplate.expire(key,Duration.ofMillis(10_000));
            return true;
        }
        return false;
    }
    public void unlock(String key,String value){
        String currrentValue =  redisTemplate.opsForValue().get(key);
        if(currrentValue != null && value.equals(currrentValue)){
            redisTemplate.delete(key);
        }
    }
}
