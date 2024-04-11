package com.la.letsassemble.chatTest;

import com.la.letsassemble.Entity.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="chatRoom")
public class ChatRoomTest {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_id")
    private Long chatId;
    private String roomName;
    private Long userCount;
    @OneToMany
    private List<Users>memberList;

    public ChatRoomTest createRoom(String roomName){
        ChatRoomTest chatRoom = new ChatRoomTest();
        chatRoom.roomName = roomName;
        return chatRoom;
    }
}
