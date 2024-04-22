package com.la.letsassemble.QueryDsl;

import com.la.letsassemble.Entity.Buy_Option;

import java.util.List;
import java.util.Optional;

public interface Buy_OptionCustomRepository {
    public List<String> searchFullDate(Boolean isOnline);
    public Long searchEven_day(String even_day,Boolean Online);
    public List<String> getUserSelectDay(Long partyId);
    public List<Buy_Option> findByEmailOrderByEven_day(String email);
    public Long Even_day_ge_TodayAndUid(String uid);

    public List<Buy_Option> findByParty_IdAndEven_day(Long partyId,String even_day);
}
