package com.la.letsassemble.QueryDsl;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.la.letsassemble.Entity.QPartyInfo.partyInfo;
import static com.la.letsassemble.Entity.QParty.party;

@Repository
@RequiredArgsConstructor
public class PartyInfoCustomRepositoryImpl implements PartyInfoCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Long> findUserCounter(List<PartyInfo> list) {
        BooleanBuilder builder = new BooleanBuilder();
        for(PartyInfo p : list){
            builder.or(partyInfo.party.id.eq(p.getParty().getId()));
            builder.and(partyInfo.state.eq("Y"));
        }
        return jpaQueryFactory.select(partyInfo.party.count())
                .from(partyInfo)
                .where(builder)
                .groupBy(partyInfo.party.id)
                .fetch();
    }
    @Override
    public List<Long> findUserCounter_Party(List<Party> list) {
        BooleanBuilder builder = new BooleanBuilder();
        for(Party p : list){
            builder.or(partyInfo.party.id.eq(p.getId()));
            builder.and(partyInfo.state.eq("Y"));
        }
        return jpaQueryFactory.select(partyInfo.party.count())
                .from(partyInfo)
                .where(builder)
                .groupBy(partyInfo.party.id)
                .fetch();
    }

}
