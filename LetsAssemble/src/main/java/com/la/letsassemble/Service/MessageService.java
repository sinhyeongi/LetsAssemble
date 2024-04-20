package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.*;
import com.la.letsassemble.Repository.MessageReadRepository;
import com.la.letsassemble.Repository.MessageRepository;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Util.MessegeFilter;
import com.la.letsassemble.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository repo;
    private final PartyRepository partyRepository;
    private final PartyInfoRepository partyInfoRepository;
    private final MessegeFilter filter;
    private final MessageReadRepository messageReadRepository;

    public Message save(Message msg){
        return repo.saveAndFlush(msg);
    }
    public Optional<Message> findByMessageId(Long id){
        return repo.findById(id);
    }
    @Transactional
    public MessageDTO SendChatBefore(Long partyId, String msg, Users u){
        Party party = partyRepository.findById(partyId).orElse(null);
        if(party == null || u == null || msg == null || msg.isBlank()){
            return null;
        }

        Message message = Message.builder()
                .party_id(party)
                .user(u)
                .content(msg)
                .build();
        message= save(message);
        List<PartyInfo> list = partyInfoRepository.findByParty(party);
        for(PartyInfo p : list){
            MessageRead messageRead = MessageRead.builder()
                    .message_id(message)
                    .email(u)
                    .build();
            messageRead = messageReadRepository.save(messageRead);
            if( messageRead == null){
                return null;
            }
        }


        if(message != null && message.getContent() != null){
            message.setContent(filter.filterMessage(message.getContent()));
        }
        return MessageDTO.builder()
                .message(message)
                .build();
    }
    public List<MessageDTO> findByPartyIdLimit30(Long partyId){
        List<MessageDTO> list = repo.findPartyIdAndLimit30(partyId);
        if(list != null){
            Collections.reverse(list);
        }
        return list;
    }
}
