package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.MailSendService;
import com.la.letsassemble.Service.UsersService;

import com.la.letsassemble.dto.PasswordForm;
import com.la.letsassemble.dto.UserForm;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;
    private final MailSendService mailSendService;
    
    @GetMapping("")
    public String signupForm(@Nullable @AuthenticationPrincipal PricipalDetails details, Model model,HttpServletResponse response) throws IOException {
        if(details != null){//소셜로그인이 존재시
            Users u = details.getUser();
            //기존회원이 접근시.
            if(usersService.findByEmail(u.getEmail()).isPresent()){
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
            usersService.UpdatePricipal(u);
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
    @GetMapping("/loginFail")
    public String LoginFail(@RequestParam boolean error,@RequestParam String exception,Model model){
        model.addAttribute("error",error);
        model.addAttribute("exception",exception);
        return "ilmo_loginForm";
    }
    @GetMapping("/ilmo_login")
    public String ilmo_login(){
        return "ilmo_loginForm";
    }

    @GetMapping("/forgot")
    public String forgotPasswordForm(){
        return "forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(HttpServletResponse response,String email,String authNumber,Model model) throws IOException {

        if (mailSendService.CheckAuthNum(email, authNumber)) {
            model.addAttribute("email",email);
            return "resetPassword";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "error";
        }
    }
    @PostMapping("/resetPassword")
    public @ResponseBody String reset(@RequestBody PasswordForm form){
        Optional<Users> u =usersService.findByEmail(form.getEmail());
        Users user = u.orElse(null);
        return usersService.changePassword(user,form);
    }

    @PostMapping("/changeNickname")
    public @ResponseBody ResponseEntity<String> changeNickname(@Nullable @AuthenticationPrincipal PricipalDetails userDetails,@RequestBody UserForm userForm){
        return usersService.changeNickname(userDetails,userForm);
    }

}
