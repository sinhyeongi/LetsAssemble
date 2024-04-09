package com.la.letsassemble;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Util.InicisUtil;
import com.la.letsassemble.Util.MessegeFilter;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest

class LetsAssembleApplicationTests {
    @Autowired
    InicisUtil util;
    @Test
    void tet1(){
        Map<String,String> list = new HashMap<>();
        list.put("test1","test");
        list.put("test2","test");
        list.put("test3","test");
        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(list.toString());
        System.out.println("obj = " + obj);
        //System.out.println("token = " + util.getPrice());
    }
    @Test
    void test2(){
        String u = "2024-03-09 15:32:41/010-1234-1234/test";
        String price = util.getPrice(u);
        String cancel = util.getCancel_Amount(u);
        System.out.println("cancel = " + cancel);
        System.out.println("price = " + price);
        int val = util.RefundableAmount(u);
        System.out.println("val = " + val);
        System.out.println("util = " + util.Cancel(u,300));
    }
}
