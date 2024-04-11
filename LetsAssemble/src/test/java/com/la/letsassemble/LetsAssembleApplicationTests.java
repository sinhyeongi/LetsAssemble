package com.la.letsassemble;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.*;
import com.la.letsassemble.Service.Buy_OptionService;
import com.la.letsassemble.Util.InicisUtil;
import com.mysema.commons.lang.Assert;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class LetsAssembleApplicationTests {
    @Autowired
    InicisUtil util;
    @Autowired
    Buy_OptionRepository buy_repo;
    @Autowired
    Buy_OptionService buyOptionService;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    UsersRepository repo;
    @Autowired
    PartyRepository partyRepository;
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext wac;
//    @BeforeEach
//    void setUp(){
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
//    @Test
//    void test2() throws JsonProcessingException, InterruptedException {
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//        List<Map<String,Object>> reqdata = new ArrayList<>();
//        Map<String,Object> list = new HashMap<>();
//        list.put("partyId","1");
//        list.put("even_day","2024-04-12");
//        list.put("price","1000");
//        list.put("name","파티글 상단에 고정하기");
//        list.put("email","test@test.tes");
//        list.put("imp_uid","2024-04-11 17:05:24 외2");
//        list.put("isOnline","false");
//        reqdata.add(list);
//        reqdata.add(list);
//        reqdata.add(list);
//        ObjectMapper mapper = new ObjectMapper();
//        String va = mapper.writeValueAsString(reqdata);
//        System.out.println("va = " + va);
//        for(int i = 0 ; i < threadCount; i++){
//            executorService.submit(()->{
//               try{
//                   mockMvc.perform(MockMvcRequestBuilders.post("/pay/add")
//                           .contentType("application/json")
//                           .content(va)
//                           ).andExpect(MockMvcResultMatchers.status().isOk());
//               }catch (Exception e){
//                   e.printStackTrace();
//               }
//               finally {
//                   latch.countDown();
//               }
//            });
//        }
//        latch.await();
//        System.out.println(buy_repo.searchEven_day("2024-04-12") == 4);
//        System.out.println(buy_repo.searchEven_day("2024-04-12"));
//    }
}
