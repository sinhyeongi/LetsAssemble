package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Message;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.MessageReadRepository;
import com.la.letsassemble.Repository.MessageRepository;
import com.la.letsassemble.Repository.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageReadService {
    private final MessageReadRepository repo;
    private final MessageRepository messageRepository;
    private final RedisLockRepository redisLockRepository;
    @Transactional
    public int DeleteMessageByUsers(Long messageId, Users u){
        boolean ch = false;
        try{
            int count = 0;
            while(!(ch=redisLockRepository.lock("MessageRead","del")) && count <= 10){
                Thread.sleep(1000);
                count++;
            }

            Message message = messageRepository.findById(messageId).orElse(null);
            if(message == null){
                return 0;
            }
            redisLockRepository.unlock("MessageRead","del");
            ch = false;
            return repo.deleteByMessageAndUsers(message, u);
        }catch (Exception e){

        }finally {
            if(ch){
                redisLockRepository.unlock("MessageRead","del");
            }
        }
        return 0;
    }
}
