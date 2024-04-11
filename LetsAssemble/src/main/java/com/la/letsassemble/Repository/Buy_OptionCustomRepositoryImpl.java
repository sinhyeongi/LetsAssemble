package com.la.letsassemble.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import static com.la.letsassemble.Entity.QBuy_Option.buy_Option;


@Repository
@RequiredArgsConstructor
public class Buy_OptionCustomRepositoryImpl implements Buy_OptionCustomRepository {
    private final JPAQueryFactory factory;
    @Override
    public List<String> searchFullDate(Boolean isOnline) {
        BooleanBuilder builder = new BooleanBuilder();
        int max_count = 8;
        if(isOnline != null){
            max_count = 4;
            builder.and(buy_Option.isOnline.eq(isOnline));
        }
        return factory.select(buy_Option.even_day)
                .from(buy_Option)
                .where(builder)
                .groupBy(buy_Option.even_day)
                .having(buy_Option.even_day.count().goe(max_count).and(buy_Option.even_day.goe(LocalDate.now().toString())))
                .fetch();

    }

    @Override
    public Long searchEven_day(String even_day) {
        return factory.select(buy_Option.count())
                .from(buy_Option)
                .where(buy_Option.even_day.eq(even_day))
                .fetchOne();
    }
}
