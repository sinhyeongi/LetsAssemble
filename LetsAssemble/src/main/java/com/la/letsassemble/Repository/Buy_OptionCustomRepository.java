package com.la.letsassemble.Repository;

import java.util.List;

public interface Buy_OptionCustomRepository {
    public List<String> searchFullDate(Boolean isOnline);
    public Long searchEven_day(String even_day);
}
