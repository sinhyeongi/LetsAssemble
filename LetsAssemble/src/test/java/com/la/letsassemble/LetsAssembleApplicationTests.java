package com.la.letsassemble;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class LetsAssembleApplicationTests {
    @Autowired
    UsersRepository urepo;
    @Autowired
    PartyInfoRepository painrepo;
    @Autowired
    PartyRepository parepo;
    @PostConstruct
    void init(){
        Users u = Users.builder()
                .email("test")
                .password("test")
                .phone("010-1234-1234")
                        .name("test")
                                .nickname("test")
                                        .gender("M")
                                                .age(123)
                .build();

        urepo.save(u);
        List<Users> list= urepo.findAll();
        for (Users u2 : list){
            System.out.println(u2);
        }
    }
    @Test
    void test1(){
        System.out.println("End");
    }
}
