package com.la.letsassemble.QueryDsl;

import com.la.letsassemble.Entity.Party;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.la.letsassemble.Entity.QParty.party;

@Repository
@RequiredArgsConstructor
public class PartyCustomRepositoryImpl implements PartyCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<String> GetArea() {
        return jpaQueryFactory.select(party.area)
                .from(party)
                .groupBy(party.area)
                .fetch();
    }

}
