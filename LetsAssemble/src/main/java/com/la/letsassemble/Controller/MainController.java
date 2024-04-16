package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import jakarta.annotation.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class MainController {
    @GetMapping(value = {"/",""})
    public String Home(){
        return "Home";
    }



    @GetMapping("/qna") // q & a
    public String test(){ return "qna"; }


    @ResponseBody
    @GetMapping("/oauth2/endpoint") // 테스트
    public String Oauth2Login(@AuthenticationPrincipal PricipalDetails details){
        return details.getUser().toString();
    }


}
