package com.la.letsassemble.Controller;


import com.la.letsassemble.Entity.Users;

import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.Buy_OptionService;
import com.la.letsassemble.Service.PartyService;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/pay")
@RequiredArgsConstructor
public class OptionCotroller {
    private final Buy_OptionService service;
    private final PartyService partyService;
    private final UsersRepository usersRepository;
    //옵션 선택 및 결제 페이지 이동
    @GetMapping(value = {"/{partyId}"})
    public String optionPage(Model model, @PathVariable Long partyId){
//        if(partyId == null){
//            return "/";
//        }

        PricipalDetails details = new PricipalDetails(usersRepository.findByEmail("test@test.tes").get());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("auth.getPrincipal() instanceof PricipalDetails = "+(auth.getPrincipal() instanceof PricipalDetails));
        if(auth.getPrincipal() instanceof PricipalDetails) {
            PricipalDetails pdetails = (PricipalDetails)auth.getPrincipal();
            Users u = pdetails.getUser();
            model.addAttribute("user",u);
        }


        String imp = "imp62080161";
        model.addAttribute("imp_data",imp);

        model.addAttribute("party",partyService.findByPartyId(1L).get());
        return "option_payment";
    }
    //결제 추가
    @PostMapping(value = "/add")
    public @ResponseBody String addPayData(@RequestBody List<Map<String,Object>> reqdata, HttpServletResponse response){
        String msg = service.add(reqdata,response);
        return msg;
    }


}
