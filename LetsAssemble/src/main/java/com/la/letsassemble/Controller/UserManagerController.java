package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.PartyInfoService;
import com.la.letsassemble.Service.PartyService;
import com.la.letsassemble.Service.UsersService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/info")
@RequiredArgsConstructor
@Slf4j
public class UserManagerController {
    private final PartyService partyService;
    private final UsersService usersService;
    private final PartyInfoService partyInfoService;
    @GetMapping("")
    public String myPage(@Nullable @AuthenticationPrincipal PricipalDetails userDetails, Model model){
        if(userDetails == null){
            return "redirect:/user/loginForm";
        }
        Users user = userDetails.getUser();
        model.addAttribute("user",user);
        return "myPage";
    }

    @GetMapping("/userinfo")
    public String userInfo(@Nullable @AuthenticationPrincipal PricipalDetails userDetails, Model model){
        if(userDetails == null){
            return "redirect:/user/loginForm";
        }
        Users user = userDetails.getUser();
        model.addAttribute("user",user);
        return "userInfo";
    }

    @GetMapping("/myParty")
    public String myParty(@Nullable @AuthenticationPrincipal PricipalDetails userDetails, Model model){
        if(userDetails == null){
            return "redirect:/user/loginForm";
        }
        Users user = userDetails.getUser();
        List<Party> myParty_List = partyService.findAllByUser(user);
        for(Party p :myParty_List){
            log.error("myParty = {}",p.toString());
        }
        List<Party> joined_List = partyService.findAllByUserAndState(user,"Y");
        for(Party p :joined_List){
            log.error("Joined p = {}",p.toString());
        }
        List<Party> request_List = partyService.findAllByUserAndState(user,"W");
        for(Party p :request_List){
            log.error("request p = {}",p.toString());
        }
        model.addAttribute("joinedPartyList",joined_List);
        model.addAttribute("myPartyList",myParty_List);
        model.addAttribute("requestParty",request_List);
        model.addAttribute("user",user);
        return "myParty";
    }

    @GetMapping("/status/{partyId}")
    public @ResponseBody List<PartyInfo> status(@PathVariable Long partyId, @Nullable @AuthenticationPrincipal PricipalDetails userDetails, Model model){
        if(userDetails == null){
            return null;
        }
        Party party = partyService.findByPartyId(partyId).orElse(null);
        if(party ==null){
            return null;
        }
        if(!party.getUser().getEmail().equals(userDetails.getUser().getEmail())){
            return null;
        }
        List<PartyInfo> partyInfoList = partyInfoService.findAllByPartyId(party);
        return partyInfoList;
    }



}
