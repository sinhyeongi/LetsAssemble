package com.la.letsassemble.QueryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Buy_OptionCustomRepositoryImpl implements Buy_OptionCustomRepository{
    private final JPAQueryFactory factory;

}
