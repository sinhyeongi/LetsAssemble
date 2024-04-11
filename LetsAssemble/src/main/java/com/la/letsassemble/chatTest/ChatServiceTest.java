package com.la.letsassemble.chatTest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceTest {
    private final ChatRepositoryTest chatRepositoryTest;
    private final ChatMemberRepositoryTest memberRepositoryTest;
    private final ChatDtoTest dtoTest;


    public List<ChatRoomTest> getAllChatRoom(){
        return chatRepositoryTest.findAll();
    }
    // roomId 기준으로 채팅방 찾기
    public ChatRoomTest findByRoomId(Long roomId){
        return chatRepositoryTest.findById(roomId).orElse(null);
    }
    // roomName 으로 채팅방 만들기
    @Transactional
    public ChatRoomTest createChatRoom(String roomName){
        ChatRoomTest chatRoomTest = new ChatRoomTest().createRoom(roomName);
        return chatRepositoryTest.save(chatRoomTest);
    }
    @Transactional
    public void increaseUser(String roomId){
        ChatRoomTest chatRoomTest = chatRepositoryTest.findByRoomName(roomId);
        chatRoomTest.setUserCount(chatRoomTest.getUserCount()+1);
        chatRepositoryTest.save(chatRoomTest);
    }
    @Transactional
    public void decreaseUser(String roomId){
        ChatRoomTest chatRoomTest = chatRepositoryTest.findByRoomName(roomId);
        chatRoomTest.setUserCount(chatRoomTest.getUserCount()-1);
        chatRepositoryTest.save(chatRoomTest);
    }
    @Transactional
    public String joinChatRoom(Long chatId,Long userId){
        //채팅방에 유저있는지 확인
        if(memberRepositoryTest.findByChatIdAndUserid(chatId,userId).isPresent()){
            return "이미 가입된 채팅방입니다.";
        }
        ChatMemberTest chatMemberTest = new ChatMemberTest().createChatMember(chatId,userId);
        memberRepositoryTest.save(chatMemberTest);

        return "채팅방에 가입되었습니다.";
    }
    
    //상태 변경
    @Transactional
    public void changeChatMember(Long chatId, Long userId,String status){
        ChatMemberTest chatMemberTest = memberRepositoryTest.findByChatIdAndUserid(chatId,userId).orElse(null);
        if(chatMemberTest != null){
            chatMemberTest.changeStatus(chatMemberTest,status);
            memberRepositoryTest.save(chatMemberTest);
        }
    }

}
