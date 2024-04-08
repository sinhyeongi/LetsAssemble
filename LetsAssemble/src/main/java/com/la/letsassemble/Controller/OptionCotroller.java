package com.la.letsassemble.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.la.letsassemble.Entity.Buy_Option;
import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;

import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.Buy_OptionService;
import com.la.letsassemble.Service.PartyService;
import com.la.letsassemble.Service.UsersService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/pay")
@RequiredArgsConstructor
public class OptionCotroller {
    private final Buy_OptionService service;
    private final PartyService partyService;
    private final UsersRepository usersRepository;
    //옵션 선택 및 결제 페이지 이동
    @GetMapping(value = {"/",""})
    public String optionPage(Model model, @RequestParam Long partyId, SecurityContext context){
        if(partyId == null){
            return "/";
        }
        String imp = "imp62080161";
        model.addAttribute("imp_data",imp);
        model.addAttribute("party",partyService.findByPartyId(1L));
        return "option_payment";
    }
    //결제 추가
    @PostMapping(value = "/add")
    public @ResponseBody String addPayData(@RequestBody List<Map<String,Object>> reqdata, HttpServletResponse response){
        String msg = service.add(reqdata,response);
        return msg;
    }


}
