package com.la.letsassemble.dto;

import com.la.letsassemble.Entity.Message;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Query;

@Data
@NoArgsConstructor
public class MessageDTO {
    Long id;
    String writer; // 작성자
    String content; // 내용
    String date;//보낸 시간
    Long partyId;
    @Builder
    MessageDTO(Message message){
        id = message.getId();
        writer = message.getUser().getEmail();
        content = message.getContent();
        date = message.getTDate();
        partyId = message.getParty_id().getId();
    }
    @QueryProjection
    public MessageDTO(Long id,String writer,String content,String date,Long partyId){
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.date = date;
        this.partyId = partyId;
    }
}
