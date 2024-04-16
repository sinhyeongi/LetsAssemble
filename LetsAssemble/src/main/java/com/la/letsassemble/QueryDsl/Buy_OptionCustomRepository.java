package com.la.letsassemble.QueryDsl;

import com.la.letsassemble.Entity.Buy_Option;

import java.util.List;

public interface Buy_OptionCustomRepository {
    public List<String> searchFullDate(Boolean isOnline);
    public Long searchEven_day(String even_day);
    public List<String> getUserSelectDay(String email);
    public List<Buy_Option> findByEmailOrderByEven_day(String email);
}
