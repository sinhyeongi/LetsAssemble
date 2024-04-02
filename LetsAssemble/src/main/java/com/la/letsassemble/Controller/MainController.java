package com.la.letsassemble.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {
    @GetMapping("/")
    public String Home(){
        return "hoem";
    }
}
