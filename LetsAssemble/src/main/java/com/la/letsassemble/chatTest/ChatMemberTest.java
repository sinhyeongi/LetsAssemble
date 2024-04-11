package com.la.letsassemble.chatTest;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class ChatMemberTest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userid;
    @Column(name="chat_id")
    private Long chatId;
    private Long messageCount; //보낸메세지 수
    
    private LocalDate joinDate;
    private String status;// 추방,탈퇴 등

    public ChatMemberTest createChatMember(Long userid,Long chatId){
        ChatMemberTest chatMemberTest = new ChatMemberTest();
        chatMemberTest.userid = userid;
        chatMemberTest.chatId = chatId;
        chatMemberTest.joinDate = LocalDate.now();
        return chatMemberTest;
    }

    public ChatMemberTest changeStatus(ChatMemberTest chatMemberTest, String status){
        chatMemberTest.setStatus(status);
        return chatMemberTest;
    }
}
