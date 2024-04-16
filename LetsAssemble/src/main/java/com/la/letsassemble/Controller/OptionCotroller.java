package com.la.letsassemble.Controller;


import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;

import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.Buy_OptionService;
import com.la.letsassemble.Service.PartyService;


import jakarta.annotation.Nullable;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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
    public String optionPage(Model model, @Nullable @PathVariable Long partyId,@Nullable @AuthenticationPrincipal PricipalDetails userdetail,
                             HttpServletResponse response) throws IOException {
        if(partyId == null || userdetail == null){
            return "redirect:/";
        }
        Optional<Party> party = partyService.findByPartyId(partyId);
        Users u = userdetail.getUser();
        if(party.isEmpty()){
            return "redirect:/";
        }
        Party p = party.get();
        if(u.getPhone() == null){
            return "redirect:/user";
        }
        if(!p.getUser().getEmail().equals(u.getEmail())){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }


        String imp = "imp62080161";
        model.addAttribute("disabledDates",service.getDisabledDates(p.isOnline(),u.getEmail()));
        model.addAttribute("imp_data",imp);
        model.addAttribute("party",p);
        model.addAttribute("user",u);
        return "option_payment";
    }
    @GetMapping(value = "/add")
    public void GetRequestAdd(HttpServletResponse response)throws IOException{
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
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
    @GetMapping("/view")
    public String OptionViewForm(Model model,@Nullable @AuthenticationPrincipal PricipalDetails details){
        if(details == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if(details.getUser().getPhone() == null || details.getUser().getEmail() == null){
            return "redirect:/user/";
        }
        model.addAttribute("list",service.findByUserEmail(details.getUser().getEmail()));
        return "option_view";
    }
    @DeleteMapping("/cancel/{no}")
    public @ResponseBody String Cancel(@PathVariable Long no,@AuthenticationPrincipal PricipalDetails details){
        if(details == null || details.getUser().getEmail() == null || details.getUser().getPhone() == null){
            return "NFU";
        }
        return service.Cancel_Event(no,details.getUser().getEmail());
    }
}
