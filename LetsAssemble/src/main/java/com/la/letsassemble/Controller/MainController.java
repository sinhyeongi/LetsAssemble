package com.la.letsassemble.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class MainController {
    @GetMapping("/")
    public String Home(){
        return "Home";
    }
    @PostMapping("/login")
    public void Login(){}
    @GetMapping("/pay")
    public String pay(){
        return "option_payment";
    }

}
