package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Buy_Option;
import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.Buy_OptionRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Util.PriceUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Buy_OptionService {
    private final Buy_OptionRepository repo;
    private final UsersRepository usersRepository;
    private final PartyRepository partyRepository;
    private final PriceUtil priceUtil;
    @Transactional
    public String add(List<Map<String,Object>> reqdata, HttpServletResponse response){
        String msg = CheckJsonData(reqdata);
        if(!msg.equals("ok")){
            return msg;
        }

        int oprice = getTotalPrice(reqdata);
        int uprice = 0;

        List<Map<String,Object>> result = new ArrayList<>();
        for(Map<String,Object>n : reqdata) {
            uprice += Integer.parseInt(n.get("price").toString());
            Long partyId = Long.parseLong(n.get("partyId").toString());
            String userEmail = n.get("email").toString();
            Optional<Party> party = partyRepository.findById(partyId);
            Optional<Users> user = usersRepository.findByEmail(userEmail);
            if (party.isEmpty()) {
                return "'partyId' value does not exist";
            } else if (user.isEmpty()) {
                return "'email' value does not exist";
            }
            Buy_Option option = new Buy_Option().builder()
                    .party(party.get())
                    .even_day(n.get("even_day").toString())
                    .price(Integer.parseInt(n.get("price").toString()))
                    .name(n.get("name").toString())
                    .user(user.get())
                    .imp_uid(n.get("imp_uid").toString())
                    .isOnline(false)
                    .build();
            option = repo.saveAndFlush(option);
            if (option == null) {
                return "Err";
            }
            result.add(n);
        }
        if(oprice != uprice){
            return "Err price";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return result.toString();
    }

    private String CheckJsonData(List<Map<String,Object>> reqdata){
        String Numreg ="^[0-9]*$";
        String Emailreg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        String Datereg = "(19|20)\\d{2}\\-((11|12)|(0?(\\d)))\\-(30|31|((0|1|2)?\\d))";
        for(Map<String,Object> n : reqdata){
            if(n.isEmpty()){
                return "Empty Data";
            }else if(n.get("even_day") == null || n.get("even_day").toString().isBlank()){
                return "Empty even_day";
            }else if(!Pattern.matches(Datereg,n.get("even_day").toString())){
                return "even_day is Abnormal values";
            }
            else if(n.get("partyId") == null || n.get("partyId").toString().isBlank()){
                return "Empty partyId";
            }else if(!Pattern.matches(Numreg,n.get("partyId").toString())){
                return "partyId is Abnormal values";
            }
            else if(n.get("price") == null || n.get("price").toString().isBlank()){
                return "Empty Price";
            }else if(!Pattern.matches(Numreg,n.get("price").toString())){
                return "price is Abnormal values";
            }
            else if(n.get("imp_uid") == null || n.get("imp_uid").toString().isBlank()){
                return "Empty uid";
            }else if(n.get("isOnline") == null || n.get("isOnline").toString().isBlank()){
                return "Empty Online";
            }else if(n.get("email") == null || n.get("email").toString().isBlank()){
                return "Empty Email";
            }else if(!Pattern.matches(Emailreg,n.get("email").toString())){
                return "Email is Abnormal values";
            }
            else if(n.get("name") == null || n.get("name").toString().isBlank()){
                return "Empty Name";
            }
        }

        return "ok";
    }
    private int getTotalPrice(List<Map<String,Object>>list){
        int price = 0;
        for(Map<String,Object>n : list){
            Integer i =priceUtil.getPrice(n.get("name").toString());
            if( i != null) {
                price += i;
            }
        }
        return price;
    }
}
