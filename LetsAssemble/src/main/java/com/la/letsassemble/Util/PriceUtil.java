package com.la.letsassemble.Util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Getter
@Setter
public class PriceUtil {
    private HashMap<String,Integer> price;
    PriceUtil(){
        price = new HashMap<>();
        price.put("파티글 상단에 고정하기",1000);
    }
    public Integer getPrice(String key){
        return price.get(key);
    }
}
