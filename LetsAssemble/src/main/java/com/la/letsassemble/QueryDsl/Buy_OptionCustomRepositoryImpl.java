package com.la.letsassemble.QueryDsl;

import com.la.letsassemble.Entity.Buy_Option;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.la.letsassemble.Entity.QBuy_Option.buy_Option;


@Repository
@RequiredArgsConstructor
public class Buy_OptionCustomRepositoryImpl implements Buy_OptionCustomRepository {
    private final JPAQueryFactory factory;
    @Override
    public List<String> searchFullDate(Boolean isOnline) {
        BooleanBuilder builder = new BooleanBuilder();
        int max_count = 4;
        if(isOnline != null){
            builder.and(buy_Option.isOnline.eq(isOnline));
        }


        return factory.select(buy_Option.even_day)
                .from(buy_Option)
                .where(builder)
                .groupBy(buy_Option.even_day)
                .having(buy_Option.even_day.count().goe(max_count).and(buy_Option.even_day.gt(LocalDate.now().toString())))
                .fetch();

    }

    @Override
    public List<String> getUserSelectDay(Long partyId) {
        return factory.select(buy_Option.even_day)
                .from(buy_Option)
                .where(buy_Option.party.id.eq(partyId))
                .fetch();
    }

    @Override
    public Long searchEven_day(String even_day,Boolean Online) {
        return factory.select(buy_Option.count())
                .from(buy_Option)
                .where(buy_Option.even_day.eq(even_day).and(buy_Option.party.isOnline.eq(Online)))
                .fetchOne();
    }

    @Override
    public List<Buy_Option> findByEmailOrderByEven_day(String email) {
        return factory.select(buy_Option)
                .from(buy_Option)
                .where(buy_Option.user.email.eq(email))
                .orderBy(buy_Option.even_day.desc())
                .fetch();
    }

    /*
    * Test환경으로 인한 부분 환불 불가에 대한 추가 로직
    * */
    @Override
    public Long Even_day_ge_TodayAndUid(String uid) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(buy_Option.even_day.goe(LocalDate.now().toString()));
        builder.and(buy_Option.impUid.eq(uid));
        return factory.select(buy_Option.count())
                .from(buy_Option)
                .where(builder)
                .groupBy(buy_Option.impUid)
                .fetchOne();
    }

    @Override
    public List<Buy_Option> findByParty_IdAndEven_day(Long partyId, String even_day) {
        return factory.select(buy_Option)
                .from(buy_Option)
                .where(buy_Option.party.id.eq(partyId).and(buy_Option.even_day.eq(even_day)))
                .fetch();
    }
}
