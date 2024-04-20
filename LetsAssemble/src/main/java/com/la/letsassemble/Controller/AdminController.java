package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Service.PartyService;
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
public class AdminController {
    private final UsersService usersService;
    private final PartyService partyService;
    @GetMapping("")
    String admin_main(){
        return "admin";
    }

    @GetMapping("/manage_siren")
    public String manage_siren(){
        return "manage_siren";
    }

    @GetMapping("/manage_party")
    public String manage_party(Model model){
        List<Party> list = partyService.findAllParty();
        model.addAttribute("list",list);
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

    @DeleteMapping("/deleteParty")
    @ResponseBody
    public ResponseEntity<String> deleteParty(@RequestParam Long partyId) {
        // 회원 삭제 로직 수행
        partyService.deleteUser(partyId);
        List<Party> list = partyService.findAllParty();
        return ResponseEntity.ok("파티 해체가 성공적으로 처리되었습니다.");
    }
}

