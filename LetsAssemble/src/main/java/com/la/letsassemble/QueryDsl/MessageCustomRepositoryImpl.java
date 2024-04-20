package com.la.letsassemble.QueryDsl;

import com.la.letsassemble.Entity.Message;
import com.la.letsassemble.Entity.QBuy_Option;
import com.la.letsassemble.dto.MessageDTO;
import com.la.letsassemble.dto.QMessageDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.la.letsassemble.Entity.QMessage.message;

@RequiredArgsConstructor
@Repository
public class MessageCustomRepositoryImpl implements MessageCustomRepository{
    private final JPAQueryFactory factory;

    @Override
    public List<MessageDTO> findPartyIdAndLimit30(Long partyId) {
        return factory.select(new QMessageDTO(message.id,
                        message.user.email
                        ,message.content
                        ,message.TDate
                        ,message.party_id.id))
                .from(message)
                .where(message.party_id.id.eq(partyId))
                .limit(1000L)
                .orderBy(message.id.desc())
                .fetch();

    }
}
