package com.la.letsassemble.chatTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepositoryTest extends JpaRepository<ChatRoomTest,Long> {
    public ChatRoomTest findByRoomName(String name);
}
