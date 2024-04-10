package com.la.letsassemble;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.Buy_OptionCustomRepository;
import com.la.letsassemble.Repository.Buy_OptionRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Util.InicisUtil;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SpringBootTest
@Transactional
class LetsAssembleApplicationTests {
    @Autowired
    InicisUtil util;
    @Autowired
    Buy_OptionRepository buy_repo;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    UsersRepository repo;
    @Autowired
    PartyRepository partyRepository;

    @Test
    void test2(){
        System.out.println("size = "+partyRepository.findAll().size());
        System.out.println(buy_repo.searchFullDate(null));
    }
}
