package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.UsersService;

import com.la.letsassemble.dto.UserForm;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;
    @GetMapping("")
    public String signupForm(@Nullable @AuthenticationPrincipal PricipalDetails details, Model model){
        if(details != null) {
            Users u = details.getUser();
            model.addAttribute("user", u);
            if (u.getNickname()!= null) {
                return "redirect:/";
            }
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

    @PostMapping("/login")
    public void Login(){

    }
    @GetMapping("/ilmo_loginForm")
    public String LA_LoginForm(){
        return "ilmo_loginForm";
    }

}
