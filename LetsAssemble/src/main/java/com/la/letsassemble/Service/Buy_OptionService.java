package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Buy_Option;
import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.Buy_OptionRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.RedisLockRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Util.InicisUtil;
import com.la.letsassemble.Util.PriceUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Buy_OptionService {
    private final Buy_OptionRepository repo;
    private final UsersRepository usersRepository;
    private final PartyRepository partyRepository;
    private final PriceUtil priceUtil;
    private final InicisUtil inicisUtil;
    private final RedisLockRepository redisLockRepository;
    @Transactional
    public String add(List<Map<String,Object>> reqdata, HttpServletResponse response) {
        int count = 0;
        Boolean lock = false;
        while(!(lock = redisLockRepository.lock("pay","pay"))&& count <= 20){
            try {
                Thread.sleep(1000);
            }catch(InterruptedException e){

            }
            count++;
        }

        String msg = CheckJsonData(reqdata);
        if(reqdata.get(0).get("imp_uid") == null){
            redisLockRepository.unlock("pay","pay");
            return "Empty uid";
        }
        String uid = reqdata.get(0).get("imp_uid").toString();
        if(!lock&& count >= 20){
            inicisUtil.Cancel(uid,inicisUtil.RefundableAmount(uid));
            return "Number of times exceeded";
        }
        int oprice = getTotalPrice(reqdata);

        if(!msg.equals("ok")){
            inicisUtil.Cancel(uid,inicisUtil.RefundableAmount(uid));
            redisLockRepository.unlock("pay","pay");
            return msg;
        }
        String incis_getPrice = inicisUtil.getPrice(uid);
        if( incis_getPrice == null){
            inicisUtil.Cancel(uid,inicisUtil.RefundableAmount(uid));
            redisLockRepository.unlock("pay","pay");
            return "No information found for the provided uid.";
        }
        else if(Integer.parseInt(incis_getPrice) != oprice){
            inicisUtil.Cancel(uid,inicisUtil.RefundableAmount(uid));
            redisLockRepository.unlock("pay","pay");
            return "Not match price";
        }
        int uprice = 0;
        msg = "";
        List<Map<String, Object>> result = new ArrayList<>();
        List<Buy_Option> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try {
            for (Map<String, Object> n : reqdata) {
                if (priceUtil.getPrice(n.get("name").toString()) == null) {
                    msg= "Price Err";
                    throw new Exception();
                }
                int price = Integer.parseInt(n.get("price").toString());
                Long partyId = Long.parseLong(n.get("partyId").toString());
                String userEmail = n.get("email").toString();
                String even_date = n.get("even_day").toString();

                LocalDate evenDate = LocalDate.parse(even_date);

                if(evenDate.isBefore(today)){
                    msg = "evenDay is Before";
                    throw new Exception();
                }else if(today.isEqual(evenDate)){
                    msg = "evenDay is toDay";
                    throw new Exception();
                }

                Optional<Party> party = partyRepository.findById(partyId);
                Optional<Users> user = usersRepository.findByEmail(userEmail);
                if (party.isEmpty()) {
                    msg =  "'partyId' value does not exist";
                    throw new Exception();
                } else if (user.isEmpty()) {
                    msg= "'email' value does not exist";
                    throw new Exception();
                }

                uprice += price;
                Buy_Option option = new Buy_Option().builder()
                        .party(party.get())
                        .even_day(even_date)
                        .price(Integer.parseInt(n.get("price").toString()))
                        .name(n.get("name").toString())
                        .user(user.get())
                        .imp_uid(n.get("imp_uid").toString())
                        .isOnline(party.get().isOnline())
                        .build();
                if (repo.searchEven_day(even_date) >= 4) {
                    msg= even_date + "is Full";
                    throw new Exception();
                }
                option = repo.saveAndFlush(option);

                if (option == null) {
                    msg =  "Err";
                    throw new Exception();
                }
                list.add(option);
                result.add(n);
            }
            if (oprice != uprice) {
                msg = "Err price";
                throw new Exception();
            }
        }catch(Exception e){
            redisLockRepository.unlock("pay","pay");
            inicisUtil.Cancel(uid,inicisUtil.RefundableAmount(uid));
            repo.deleteAll(list);
            return msg;
        }finally {
            redisLockRepository.unlock("pay","pay");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return "ok";
    }
    public List<String> getDisabledDates(Boolean isOnline,String email){
        List<String> list =repo.searchFullDate(isOnline);
        List<String> list2 = repo.getUserSelectDay(email);
        if(!list.isEmpty() && !list2.isEmpty()){
            list.addAll(list2);
            return list2;
        }else if(list.isEmpty()&& !list2.isEmpty()){
            return list2;
        }

        return list;
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
        int totalprice = 0;
        for(Map<String,Object>n : list){
            Integer price =priceUtil.getPrice(n.get("name").toString());
            if( price != null) {
                totalprice += price;
            }
        }
        return totalprice;
    }
}
