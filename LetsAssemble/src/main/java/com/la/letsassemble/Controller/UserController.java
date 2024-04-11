package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.UsersService;

import com.la.letsassemble.dto.EmailRequestDto;
import com.la.letsassemble.dto.UserForm;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public @ResponseBody String signup(@RequestBody UserForm form){
        Users u;
        if((u = usersService.signup(form)) != null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null && authentication.getPrincipal() instanceof PricipalDetails) {
                PricipalDetails details = (PricipalDetails) authentication.getPrincipal();
                details.setUser(u);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(details,authentication.getCredentials(),details.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            return "ok";
        }else{
            return "fail";
        }
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
