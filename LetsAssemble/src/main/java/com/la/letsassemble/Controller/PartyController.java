package com.la.letsassemble.Controller;


import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.PartyInfoService;
import com.la.letsassemble.Service.PartyService;
import com.la.letsassemble.Service.UsersService;
import com.la.letsassemble.dto.PartyForm;
import com.la.letsassemble.dto.PartyInfoForm;
import jakarta.annotation.Nullable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {
    private boolean createButtonEnabled = true;
    private boolean updateButtonEnabled = true;
    private boolean applyButtonEnabled = true;
    private final UsersRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PartyService partyService;
    private final PartyInfoService partyInfoService;

    /// test
    private final PartyRepository partyRepository;
    private final PartyInfoRepository partyInfoRepository;


    @Data
    @AllArgsConstructor
    static class party {
        private int id;
        private String title;
        private String content;
        private String intro;
        private String interest;
        private String area;
        private int personnel;
        private int isOnline;
    }

    @GetMapping("/memberStatus/{partyId}")
    public String memberStatus(HttpServletResponse response,@PathVariable Long partyId,@Nullable @AuthenticationPrincipal PricipalDetails userDetails) throws IOException {
        Long id = (partyId);
        Optional<Party> party = partyService.findByPartyId(id);
        if (!party.isPresent()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        log.info("로그인중인 유저 = {}", userDetails.getUser());
        log.info("파티 파티장 = {}", party.get().getUser());

        if (party.get().getUser().getId() != userDetails.getUser().getId()) {
            log.error("파티장 과 로그인 아이디가 같지않음.");
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
        if(!applyButtonEnabled){
            return "button";
        }
        applyButtonEnabled = false;
        //로그인상태가 아닐경우
        if(userDetails == null){
            log.error("로그인상태가 아님");
            applyButtonEnabled = true;
            return "login";
        }
        //파티가 존재하지 않을 경우
        Optional<Party> optionalParty = partyService.findByPartyId(partyId);
        if(!optionalParty.isPresent()){
            log.error("파티가 존재하지않음");
            applyButtonEnabled = true;
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        log.error("파티가 존재함");

        Party party = optionalParty.get();
        Users user = userDetails.getUser();
        String result = partyService.applyJoinParty(party,user,form);
        applyButtonEnabled = true;
        return result;
    }

    @GetMapping("/find_party")
    public String find_party(Model model){
        addTest(model);
        return "find_party";
    }
    @GetMapping("/add/{paryId}")
    @ResponseBody
    public String addParty(@AuthenticationPrincipal PricipalDetails auth, @PathVariable Long paryId){
        Users u = null;
        if(auth instanceof  PricipalDetails){
            u = auth.getUser();
        }
        PartyInfo partyInfo = PartyInfo.builder()
                .party(partyRepository.findById(paryId).orElse(null))
                .applicant_id(u)
                .state("Y")
                .isBlack(false)
                .build();
        partyInfo = partyInfoRepository.save(partyInfo);
        return partyInfo == null ? "err":"ok";
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
        if(!createButtonEnabled){
            return "button is disabled";
        }
        Users user = userDetails.getUser();
        createButtonEnabled = false;
        log.info("create party = {}",party);
        String createPartyId = partyService.createParty(party, user);
        createButtonEnabled = true;
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
        log.info("로그인중인 유저 = {}" , userDetails.getUser());
        log.info("파티 파티장 = {}" , party.get().getUser());

        if(party.get().getUser().getId() != userDetails.getUser().getId()){
            log.error("파티장 과 로그인 아이디가 같지않음.");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        model.addAttribute("party",party.get());
        return "updateParty";
    }
    @PostMapping("/update")
    public @ResponseBody String updateParty(@RequestBody PartyForm partyForm,@Nullable @AuthenticationPrincipal PricipalDetails userDetails, HttpServletResponse response) {
        if(!updateButtonEnabled)return "already work";
        updateButtonEnabled=false;
        log.info("update party = {}",partyForm );
        partyService.updateParty(partyForm);
        updateButtonEnabled = true;
        return "ok";
    }

        private void addTest(Model model) {
        List<party> bigList = new ArrayList<>();
        List<party> smallList = new ArrayList<>();

        // 더미 없어서 임의로 만든 리스트
        // 더미 있을시 유료 리스트 , 일반 리스트 넘겨주면 됨
        bigList.add(new party(3,"롤 5인큐 하실 분 구합니다 !! 아무나들어와보세요", "ㅇㅇㅇ아무나 오셈","마이크 필수 , 매너있는 사람만 오세요 ~" , "게임","서울",3, 0));
        bigList.add(new party(7,"야구 보러 가실 한화 팬 구합니다", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 1));
        bigList.add(new party(10,"야구 보러 가실 한화 팬 구합니다", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 1));
        bigList.add(new party(11,"테스트테스트테스트", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 0));
        bigList.add(new party(12,"축구 좋아하는사람", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 1));
        bigList.add(new party(13,"ㅁㄴㅇㄻㄴㅇㄻㄴㅇㄻㄴ", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 0));

        smallList.add(new party(1,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4, 0));
        smallList.add(new party(2,"코딩 스터디 구합니다" , "asdf" , "열정적으로 공부하실 분들 구해요" , "스터디" , "말레이시아" , 2 , 0));
        smallList.add(new party(5,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4 , 1));
        smallList.add(new party(4,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4, 1));
        smallList.add(new party(6,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4,1));
        smallList.add(new party(8,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4,0));
        smallList.add(new party(9,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4,1));

        model.addAttribute("big_items", bigList);

        model.addAttribute("small_items", smallList);
    }

    @GetMapping("/getParties")
    @ResponseBody
    public List<party> getPartiesByType(@RequestParam(value = "type", required = false) String type) {
//        if ("online".equals(type)) {
//            return partyService.getOnlineParties();
//        } else if ("offline".equals(type)) {
//            return partyService.getOfflineParties();
//        } else {
//            // type이 all일때
//            return partyService.getAllParties();
//        }

        // 더미 없어서 임의로 만든 리스트
        // 더미 있을시 online 리스트 , offline 리스트 출력 후 넘겨주면 됨

        List<party> onlineList = new ArrayList<>();
        List<party> offlineList = new ArrayList<>();

        List<party> bigList = new ArrayList<>();
        bigList.add(new party(3,"롤 5인큐 하실 분 구합니다 !! 아무나들어와보세요", "ㅇㅇㅇ아무나 오셈","마이크 필수 , 매너있는 사람만 오세요 ~" , "게임","서울",3, 0));
        bigList.add(new party(7,"야구 보러 가실 한화 팬 구합니다", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 1));
        bigList.add(new party(10,"야구 보러 가실 한화 팬 구합니다", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 1));
        bigList.add(new party(11,"테스트테스트테스트", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 0));
        bigList.add(new party(12,"축구 좋아하는사람", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 1));
        bigList.add(new party(13,"ㅁㄴㅇㄻㄴㅇㄻㄴㅇㄻㄴ", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4 , 0));
        for (party pt : bigList) {
            if (pt.getIsOnline() == 1) {
                onlineList.add(pt);
            }
        }

        for (party pt : bigList) {
            if (pt.getIsOnline() == 0) {
                offlineList.add(pt);
            }
        }
        List<party> allList = new ArrayList<>();
        int idx = 0;
        for(party pt : bigList){
            if(idx < 4 ){
            allList.add(pt);
            }
            idx += 1;
        }

         if ("online".equals(type)) {
            return onlineList;
        } else if ("offline".equals(type)) {
            return offlineList;
        } else if("all".equals(type)){
             return allList;
         }
         return null;
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
            log.error("party 정보 = {}",party);
            log.error("info 정보 = {}",optionalPartyInfo.orElse(null));
            if(optionalPartyInfo.isPresent()){
                PartyInfo partyInfo = optionalPartyInfo.get();
                String state = partyInfo.getState();
                log.error("가입 상태 = {}",state);
                model.addAttribute("state", state);
            }
        }
            model.addAttribute("party", party);
            return "party_info";
    }


}
