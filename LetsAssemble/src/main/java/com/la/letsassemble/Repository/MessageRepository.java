package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Message;
import com.la.letsassemble.QueryDsl.MessageCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message,Long>, MessageCustomRepository {

}
