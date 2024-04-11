package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.UsersService;

import com.la.letsassemble.dto.EmailRequestDto;
import com.la.letsassemble.dto.UserForm;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;

    @GetMapping("")
    public String signupForm(@Nullable @AuthenticationPrincipal PricipalDetails details, Model model){
        if(details != null){
            Users u = details.getUser();
            if(u.getNickname()!= null){
                return "redirect:/";
            }
            model.addAttribute("user",u);
        }
        return "signup";
    }
    @PostMapping("")
    public String signup(@ModelAttribute UserForm form){
        usersService.signup(form);
        return "redirect:/";
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
    
    @GetMapping("/ilmo_login")
    public String ilmo_login(){
        return "ilmo_loginForm";
    }

}
