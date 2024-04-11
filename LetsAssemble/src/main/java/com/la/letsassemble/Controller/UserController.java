package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.UsersService;

import com.la.letsassemble.dto.EmailRequestDto;
import com.la.letsassemble.dto.UserForm;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;
//403에러 response.sendError(HttpServletResponse.SC_FORBIDDEN);
    @GetMapping("")
    public String signupForm(@Nullable @AuthenticationPrincipal PricipalDetails details, Model model,HttpServletResponse response) throws IOException {
        if(details != null){//소셜로그인이 존재시
            Users u = details.getUser();
            //기존회원이 접근시.
            if(usersService.findByProviderAndProviderId(u.getProvider(),u.getProviderId()).isPresent()){
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            //소셜회원이면서 회원정보 없을 시
            //닉네임이 없을 경우로 함 (닉네임 not null)
            if(u.getNickname() != null){
                return "redirect:/";
            }
            model.addAttribute("user",u);
        }
        return "signup";
    }
    @PostMapping("")
    public @ResponseBody String signup(@RequestBody UserForm form){
        if(usersService.signup(form) != null){
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

    @GetMapping("/auth2/check")
    public String Auth2Login(@AuthenticationPrincipal PricipalDetails details){
        Users u =details.getUser();
        if(u.getEmail() == null|| u.getPhone() == null){
            return "redirect:/user";
        }
        System.out.println("details = " + details.getUser());
        return "redirect:/";
    }
}
