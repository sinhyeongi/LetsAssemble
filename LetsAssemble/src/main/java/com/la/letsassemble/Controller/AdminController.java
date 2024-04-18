package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final UsersService usersService;
    @GetMapping("")
    String admin_main(){
        return "admin";
    }

    @GetMapping("/manage_siren")
    public String manage_siren(){
        return "manage_siren";
    }

    @GetMapping("/manage_party")
    public String manage_party(){
        return "manage_party";
    }
    @GetMapping("/manage_user")
    public String manage_user(Model model){
        List<Users> list = usersService.findAllUsers();
        model.addAttribute("list",list);
        return "manage_user";
    }


    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        // 회원 삭제 로직 수행
        usersService.deleteUser(userId);
        List<Users> list = usersService.findAllUsers();
        return ResponseEntity.ok("회원 삭제가 성공적으로 처리되었습니다.");
    }
}

