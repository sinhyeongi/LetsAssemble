package com.la.letsassemble.Controller;


import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;

import com.la.letsassemble.Service.PartyInfoService;
import com.la.letsassemble.Service.PartyService;
import com.la.letsassemble.dto.PartyForm;
import com.la.letsassemble.dto.PartyInfoForm;
import jakarta.annotation.Nullable;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {
    private final PartyService partyService;
    private final PartyInfoService partyInfoService;


    @GetMapping("/memberStatus/{partyId}")
    public String memberStatus(HttpServletResponse response,@PathVariable Long partyId,@Nullable @AuthenticationPrincipal PricipalDetails userDetails) throws IOException {
        Long id = (partyId);
        Optional<Party> party = partyService.findByPartyId(id);
        if (!party.isPresent()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }


        if (party.get().getUser().getId() != userDetails.getUser().getId()) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        return "memberStatus";
    }

    @PutMapping("/changeStatus/{partyInfoId}")
    public @ResponseBody String changeStatus(@PathVariable Long partyInfoId, @Nullable @AuthenticationPrincipal PricipalDetails userDetails,@RequestBody PartyInfoForm form){
        if(userDetails == null) return "no login";
        Users user = userDetails.getUser();
        return partyInfoService.changeStatus(user,partyInfoId,form);
    }

    @PutMapping("/applyJoinParty/{partyId}")
    public @ResponseBody String applyjoinParty(@PathVariable Long partyId,@Nullable @AuthenticationPrincipal PricipalDetails userDetails,HttpServletResponse response,@RequestBody PartyInfoForm form) throws IOException {

        //로그인상태가 아닐경우
        if(userDetails == null || userDetails.getUser() == null){
            return "login";
        }
        //파티가 존재하지 않을 경우
        Optional<Party> optionalParty = partyService.findByPartyId(partyId);
        if(!optionalParty.isPresent()){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        Party party = optionalParty.get();
        Users user = userDetails.getUser();
        String result = partyService.applyJoinParty(party,user,form);
        return result;
    }

    @GetMapping("/find_party")
    public String find_party(Model model){
        addTest(model);
        return "find_party";
    }


    @GetMapping("/create")
    public String createPartyForm(HttpServletResponse response, @Nullable @AuthenticationPrincipal PricipalDetails userDetails) throws IOException {
        if(userDetails == null){
            return "redirect:/user/loginForm";
        }
        Users user = userDetails.getUser();
        if(user.getPhone() == null){
            return "redirect:/user";
        }
        return "createParty";
    }

    @PostMapping("/create")
    public @ResponseBody String createParty(HttpServletResponse response, @Nullable @AuthenticationPrincipal PricipalDetails userDetails, @RequestBody PartyForm party) throws IOException {
        if(userDetails == null){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        Users user = userDetails.getUser();
        String createPartyId = partyService.createParty(party, user);

        // 작업이 완료되면 버튼을 다시 활성화합니다.
        if(createPartyId != null){
            return createPartyId;
        }else{
            return "error";
        }
    }
    @GetMapping("/update/{partyId}")
    public String updatePartyForm(Model model,@PathVariable String partyId,@Nullable @AuthenticationPrincipal PricipalDetails userDetails, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(partyId);
        Optional<Party> party = partyService.findByPartyId(id);
        if(!party.isPresent()){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }


        if(party.get().getUser().getId() != userDetails.getUser().getId()){

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        model.addAttribute("party",party.get());
        return "updateParty";
    }
    @PostMapping("/update")
    public @ResponseBody String updateParty(@RequestBody PartyForm partyForm,@Nullable @AuthenticationPrincipal PricipalDetails userDetails, HttpServletResponse response) {
        if(userDetails == null || userDetails.getUser() == null){
            return "user Not Found";
        }


        partyService.updateParty(partyForm);
        return "ok";
    }

        private void addTest(Model model) {

        List<Party> bigList = partyService.findFourMoneyAllList(4); // 유료 리스트 중 4개
        List<Long> bigListCount = partyService.findUserCounter(bigList); // 파티 마다 들어가있는 사람 queryDsl
        List<Party> smallList = partyService.findAllList();  // 무료 리스트 전체

        // 지역 리스트들 보내줘야함
        List<String> allParty = partyService.getArea();

        List<PartyInfo> allPartyInfo = partyInfoService.findAllPartyInfo();

        List<Long> allpartyCount = partyService.findUserCounter(smallList);  // 파티 마다 들어가있는 사람 queryDsl
        if(allpartyCount != null){
            Collections.reverse(allpartyCount);
        }
        model.addAttribute("big_items", bigList);
        model.addAttribute("big_items_count",bigListCount);
        model.addAttribute("small_items", smallList);
        model.addAttribute("allpartyCount",allpartyCount);
        model.addAttribute("allParty", allParty);

        model.addAttribute("allPartyInfo" , allPartyInfo);
    }

    @GetMapping("/getParties")
    @ResponseBody
    public List<Party> getPartiesByType(@RequestParam(value = "type", required = false) String type) {

        if ("online".equals(type)) {
            return partyService.findFourMoneyOnlieList(4);
        } else if ("offline".equals(type)) {
            return partyService.findFourMoneyOffLineList(4);
        } else {
            // type이 all일때
            return partyService.findFourMoneyAllList(4);
        }
    }


    @GetMapping("party_info")
    public String partyInfo(@RequestParam Long id,@Nullable @AuthenticationPrincipal PricipalDetails userDetails, Model model){

        Optional<Party> optionalParty  = partyService.findByPartyId(id);

       Party party = optionalParty.orElse(null); // Optional이 비어있을 경우 null 반환
        if(party == null){
            return "/";
        }
        if(userDetails != null){
            Optional<PartyInfo> optionalPartyInfo = partyInfoService.findByPartyAndUser(party, userDetails.getUser());

            if(optionalPartyInfo.isPresent()){
                PartyInfo partyInfo = optionalPartyInfo.get();
                String state = partyInfo.getState();

                model.addAttribute("state", state);
            }
        }
            model.addAttribute("party", party);
            return "party_info";
    }
    @DeleteMapping("/disbandParty/{partyid}/partyname/{partyname}")
    @ResponseBody
    public String disbandParty(@PathVariable Long partyid,@PathVariable String partyname,@AuthenticationPrincipal PricipalDetails userDetails){
        if(userDetails == null || userDetails.getUser() == null){
            return "no login";
        }
        Users user = userDetails.getUser();
        String result = partyService.deleteParty(partyid,user,partyname);

        return result;
    }
    @PutMapping("/delegate/{partyId}/userId/{userId}")
    @ResponseBody
    public String delegate(@PathVariable Long partyId , @PathVariable Long userId, @AuthenticationPrincipal PricipalDetails userDetails){
        if(userDetails == null || userDetails.getUser() == null){
            return "no login";
        }
        Users user = userDetails.getUser();
        String result = partyService.delegateParty(partyId,user,userId);
        return result;
    }

}
