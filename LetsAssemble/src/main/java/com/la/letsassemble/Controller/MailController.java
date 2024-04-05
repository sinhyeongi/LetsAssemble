package com.la.letsassemble.Controller;

import com.la.letsassemble.Service.MailSendService;
import com.la.letsassemble.dto.EmailCheckDto;
import com.la.letsassemble.dto.EmailRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailSendService mailService;
    @PostMapping ("/mailSend")
    public String mailSend(@RequestBody @Valid EmailRequestDto emailDto){
        System.out.println("이메일 인증 이메일 :"+emailDto.getEmail());
        return mailService.joinEmail(emailDto.getEmail());
    }
    @PostMapping("/mailAuthCheck")
    public String AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
        System.out.println("getEmail = "+  emailCheckDto.getEmail());
        System.out.println("getAuthNum = "+  emailCheckDto.getAuthNum());
        if(mailService.CheckAuthNum(emailCheckDto.getEmail(),emailCheckDto.getAuthNum())){
            return "ok";
        }
        else{
            return "error";
        }
    }


}
