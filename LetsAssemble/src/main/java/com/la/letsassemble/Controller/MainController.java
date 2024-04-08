package com.la.letsassemble.Controller;

import com.la.letsassemble.Security_Custom.PricipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MainController {
    @GetMapping(value = {"/",""})
    public String Home(){
        return "Home";
    }

    @GetMapping("/login")
    public String Loginform(){
        return "loginForm";
    }

    @GetMapping("/ttt")
    public String test(){ return "qna"; }

    @PostMapping("/login")
    public void Login(){

    }
    @GetMapping("/pay")
    public String pay(){
        return "option_payment";
    }
    @ResponseBody
    @GetMapping("/oauth2/endpoint")
    public String Oauth2Login(@AuthenticationPrincipal PricipalDetails details){
        return details.getUser().toString();
    }

}
