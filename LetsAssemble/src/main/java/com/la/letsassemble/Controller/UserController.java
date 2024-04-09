package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Service.UsersService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;


    private final PasswordEncoder passwordEncoder;

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

    @GetMapping("/ilmo_loginForm")
    public String ilmo_loginForm(){
        return "ilmo_loginForm";
    }

    @PostMapping("/ilmo_loginForm")
    public String ilmo_login(String userId, String password, HttpSession session , RedirectAttributes redirectAttributes ){
           Optional<Users> users = usersService.findByEmail(userId);
        System.out.println("test !!! users = " + users);
           if(users != null && passwordEncoder.matches(password, users.get().getPassword())){
               session.setAttribute("log", userId);
               return "redirect:/user/ilmo_loginForm";
           } else {
               System.out.println("아이디 , 비밀번호 다시 확인해라 .. ");
               return "redirect:/user/ilmo_loginForm";
           }

    }



//    @PostMapping("/ilmo_loginForm")
//    public String ilmo_login(String userId, String password, HttpSession session , RedirectAttributes redirectAttributes ){
//        List<Users> userList = usersService.findByUsers();
//        String id = isValidUser(userId, password);
//        if (id != null){
//            session.setAttribute("log" , id);
//            return "redirect:/user/ilmo_loginForm";
//        } else{
//            System.out.println("아이디 , 비밀번호 다시 확인해라 .. ");
//            return "redirect:/user/ilmo_loginForm";
//        }
//    }
//
//    // 사용자 인증 예시
//    private String isValidUser(String userId, String password) {
//        // 실제로는 DB 조회 등을 통해 사용자 인증을 수행
//        // 예제에서는 간단하게 userId와 password가 일치하면 인증 성공으로 가정
//        List<Users> userList = usersService.findByUsers();
//        for(var i = 0; i < userList.size(); i+=1){
//            System.out.println("id = " + userList.get(i).getEmail());
////            if(userList.get(0).getEmail().equals(userId) && userList.get(i).getPassword().equals(password)){
//            if(userList.get(i).getEmail().equals(userId)){ // 일단 아이디만 체크 ( 테스트용 )
//                return userList.get(i).getEmail().toString();
//            }
//        }
//        return null;
//    }
}
