package com.la.letsassemble.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    // 메세지 타입 : 입장, 채팅
    public enum MessageType{
        ENTER,TALK
    }
    
    private MessageType messageType; //메세지 타입
    private Long chatRoomId; // 방 번호
    private Long senderId; //채팅을 보낸 사람
    private String message; //보낸 메세지
    private String time; // 채팅 발송 시간
}
