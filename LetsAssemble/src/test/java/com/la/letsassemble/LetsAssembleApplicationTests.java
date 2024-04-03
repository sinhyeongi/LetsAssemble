package com.la.letsassemble;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Util.MessegeFilter;
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
import java.util.ArrayList;
import java.util.Formatter;
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
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    MessegeFilter msgfilter;
    @PostConstruct
    @Commit
    void init(){
        Users u = Users.builder()
                .email("test")
                .password(encoder.encode("test"))
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
        msgfilter.print();
    }
}
