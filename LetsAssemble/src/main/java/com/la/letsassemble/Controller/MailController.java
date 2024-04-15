package com.la.letsassemble.Controller;

import com.la.letsassemble.Service.MailSendService;
import com.la.letsassemble.dto.EmailCheckDto;
import com.la.letsassemble.dto.EmailRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MailController {
    private final MailSendService mailService;
    private boolean findButtonEnabled = true;
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
    @PostMapping("/forgotEmail")
    public ResponseEntity<String> mailSend_forgot(@RequestBody @Valid EmailRequestDto emailDto, BindingResult bindingResult){
        if(!findButtonEnabled){
            return ResponseEntity.badRequest().body("buttonError");
        }
        log.info("비밀번호 찾기 이메일 = {}",emailDto.getEmail());
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("emailError");
        }
        findButtonEnabled = false;
        String result = mailService.findEmail(emailDto.getEmail());
        findButtonEnabled = true;
        if(result == "emailError"){
            return ResponseEntity.badRequest().body("userError");
        }
        if(result != "success"){
            return ResponseEntity.badRequest().body("error");
        }

        return ResponseEntity.ok("ok");
    }
}
