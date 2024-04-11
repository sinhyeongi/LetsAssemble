package com.la.letsassemble.chatTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMemberRepositoryTest extends JpaRepository<ChatMemberTest,Long> {
     Optional<ChatMemberTest> findByChatIdAndUserid(Long chatId, Long UserId);
}
