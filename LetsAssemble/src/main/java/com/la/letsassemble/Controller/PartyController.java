package com.la.letsassemble.Controller;


import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {

    private final UsersRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    private void addTest(Model model) {
        List<party> bigList = new ArrayList<>();
        List<party> smallList = new ArrayList<>();

        bigList.add(new party(3,"롤 5인큐 하실 분 구합니다 !! 아무나들어와보세요", "ㅇㅇㅇ아무나 오셈","마이크 필수 , 매너있는 사람만 오세요 ~" , "게임","서울",3));
        bigList.add(new party(7,"야구 보러 가실 한화 팬 구합니다", "ㅇㅇㅇ아무나 오셈","다른 팀 팬 오지 마세요 추방합니다" , "스포츠","충청도",4));
        smallList.add(new party(1,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4));
        smallList.add(new party(2,"코딩 스터디 구합니다" , "asdf" , "열정적으로 공부하실 분들 구해요" , "스터디" , "말레이시아" , 2));
        smallList.add(new party(5,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4));
        smallList.add(new party(4,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4));
        smallList.add(new party(6,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4));
        smallList.add(new party(8,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4));
        smallList.add(new party(9,"같이 축구하실 실력 좋은 사람 구함" , "asdf" , "내 파티 안 들어오면 지상렬" , "스포츠" , "경기도" , 4));

        model.addAttribute("big_items", bigList);
        model.addAttribute("small_items", smallList);
    }

    @GetMapping({"","/"})
    public String home(){
        return "index";
    }
    /*
    @GetMapping({"","/"})
    public String home(OAuth2AuthenticationToken authentication , Model model) {
        String providerId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
           // User u = oauth2User.getAttribute("user");
           // System.out.println("User = " + (PrincipalDetails)authentication.getPrincipal());
            //String providerId = (String) oauth2User.getAttribute("providerId");
            Map<String, Object> attributes = oauth2User.getAttributes();
            System.out.println(oauth2User);

//            providerId = (Long) attributes.get("id");
//            if (attributes.containsKey("id")) {
//                providerId = (Long) attributes.get("id");
//            }
            // 상황 1 : attributes에 "sub" 키가 있는 경우 -> google 로그인
            if (attributes.containsKey("sub")) {
                providerId = (String) attributes.get("sub");
            }

// 상황 2: attributes에 "response" 키가 있는 경우 -> naver 로그인
            if (providerId == null && attributes.containsKey("response")) {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                if (response.containsKey("id")) {
                    providerId = (String) response.get("id");
                }
            }

// 상황 3: attributes에 "id" 키가 있는 경우 -> kakao 로그인
            if (providerId == null && attributes.containsKey("id")) {
                Object idValue = attributes.get("id");
                if (idValue instanceof String) {
                    providerId = (String) idValue;
                } else if (idValue instanceof Long) {
                    providerId = String.valueOf(idValue);
                }
            }
            //System.out.println("여기서는 받는지 ? providerId = " + providerId);
            if (providerId != null) {
                // providerId가 존재하는 경우
                // 여기서 원하는 작업 수행
                System.out.println("providerId = " + providerId);
            } else {
                // providerId가 존재하지 않는 경우
                System.out.println("null입니다..ㅜㅜ");
            }
        }
        User user = userRepository.findByProviderId(providerId);
        //User user = userRepository.findByNickname(nickname);
        System.out.println("user = " + user);
        if (user.getNickname() == null) {
            // 해당 providerId를 가진 사용자가 존재하지 않는 경우, 새로운 사용자로 데이터베이스에 저장합니다.
            //user = new User();
            //user.setProviderId(providerId);
            // userRepository.save(user);

            System.out.println("email = " + user.getEmail());
            // 만약 이메일이 있으면 넘겨주기
            if(user.getEmail() != null){
                model.addAttribute("email" , user.getEmail());
            }

            System.out.println("신규 회원입니다");
            return "joinForm";
        } else {
            // 해당 providerId를 가진 사용자가 이미 존재하는 경우
            System.out.println("이미 등록된 사용자입니다");
            return "index";
        }
       // model.addAttribute("pvID", providerId);
        return "index";
    }
*/


}
