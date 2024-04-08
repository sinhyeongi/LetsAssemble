package com.la.letsassemble.Controller;

import com.la.letsassemble.Service.UsersService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;

    @GetMapping("")
    public String signupForm(){
        return "signup";
    }

    @GetMapping("/validate")
    public @ResponseBody boolean emailValidate(@RequestParam String email){
        System.out.println("param = "+ email);
        return usersService.findByEmail(email).isPresent();
    }
    @GetMapping("/validate/nickname")
    public @ResponseBody boolean nicknameValidate(@RequestParam String nickname){
        System.out.println("param = "+ nickname);
        return usersService.findByNickName(nickname).isPresent();
    }
    @GetMapping("/loginForm") // 로그인 폼 이동
    public String Loginform(){
        return "loginForm";
    }
}
