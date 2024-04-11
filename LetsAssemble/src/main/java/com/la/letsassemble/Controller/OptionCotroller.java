package com.la.letsassemble.Controller;


import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;

import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.Buy_OptionService;
import com.la.letsassemble.Service.PartyService;


import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;



import org.springframework.security.core.annotation.AuthenticationPrincipal;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/pay")
@RequiredArgsConstructor
public class OptionCotroller {
    private final Buy_OptionService service;
    private final PartyService partyService;
    private final UsersRepository usersRepository;

    //옵션 선택 및 결제 페이지 이동
    @GetMapping(value = {"/{partyId}","/"})
    public String optionPage(Model model, @Nullable @PathVariable Long partyId,@Nullable @AuthenticationPrincipal PricipalDetails userdetail){
        if(partyId == null || userdetail == null){
            return "redirect:/";
        }
        Optional<Party> party = partyService.findByPartyId(partyId);
        Users u = userdetail.getUser();
        if(party.isEmpty()){
            return "redirect:/";
        }
        Party p = party.get();
        if(!p.getUser().getEmail().equals(u.getEmail())){
            return "redirect:/";
        }


        String imp = "imp62080161";
        model.addAttribute("disabledDates",service.getDisabledDates(p.isOnline()));
        model.addAttribute("imp_data",imp);
        model.addAttribute("party",p);
        model.addAttribute("user",u);
        return "option_payment";
    }
    //결제 추가
    @PostMapping(value = "/add")
    public @ResponseBody String addPayData(@RequestBody List<Map<String,Object>> reqdata, HttpServletResponse response,@Nullable @AuthenticationPrincipal PricipalDetails details){
        if(details == null){
            return "Not Login";
        }
        String msg = service.add(reqdata,response);
        return msg;
    }


}