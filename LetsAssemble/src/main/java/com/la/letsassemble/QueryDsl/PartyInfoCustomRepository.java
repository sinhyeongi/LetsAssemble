package com.la.letsassemble.QueryDsl;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;

import java.util.List;

public interface PartyInfoCustomRepository {
    List<Long> findUserCounter(List<PartyInfo> list);
    List<Long> findUserCounter_Party(List<Party> list);
}
