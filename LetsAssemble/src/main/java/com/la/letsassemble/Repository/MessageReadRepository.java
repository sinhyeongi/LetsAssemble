package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Message;
import com.la.letsassemble.Entity.MessageRead;
import com.la.letsassemble.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReadRepository extends JpaRepository<MessageRead,Long> {
    int deleteByMessageAndUsers(Message message, Users user);
}
