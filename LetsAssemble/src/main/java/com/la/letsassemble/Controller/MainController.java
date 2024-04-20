package com.la.letsassemble.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
public class MainController {
    @GetMapping(value = {"/",""})
    public String Home(){
        return "Home";
    }



    @GetMapping("/qna") // q & a
    public String test(){ return "qna"; }




}
