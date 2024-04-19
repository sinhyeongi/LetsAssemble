package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.RedisLockRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.dto.PartyForm;
import com.la.letsassemble.dto.PartyInfoForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {
    private final PartyRepository repo;

    private final PartyInfoRepository partyInfoRepository;
    private final RedisLockRepository redisLockRepository;

    public Optional<Party> findByPartyId(Long id){
        return repo.findById(id);
    }

    @Transactional
    public String createParty(PartyForm from, Users user){
        try {

            if (user.getPhone() == null) {
                return "redirect:/user";
            }
            //버튼 동시성 방지

            //관심사가 없을 시
            if (from.getCategory() == null) {
                return "no category";
            }
            //온라인 여부 없을 시
            if (from.getIsOnline() == null) {
                return "no isOnline";
            }
            //모집인원 없을 시
            if (from.getCapacity() == null) {
                return "no capacity";
            }
            //파티이름 없을 시
            if (from.getName() == null || from.getName().trim().length() < 2) {
                return "no name";
            }
            //주소 입력 없을 시
            if (from.getAddress().isBlank()) {
                return "no address";
            }
            int count =0;
            while(!redisLockRepository.lock("party_create","party_create") && count < 10){
                Thread.sleep(1000);
                count++;
            }
            if(count >= 10){
                throw new Exception();
            }
            //파티 생성
            Party party = Party.builder()
                    .interest(from.getCategory()) //관심사
                    .title(from.getName()) //파티이름
                    .user(user)// 파티장
                    .isOnline(from.getIsOnline().equals("online"))//온라인 여부
                    .area(from.getAddress())
                    .personnel(Integer.parseInt(from.getCapacity()))
                    .build();
            //파티정보 생성
            PartyInfo partyInfo = PartyInfo.builder()
                    .party(party)
                    .applicant_id(user)
                    .state("Y")
                    .isBlack(false)
                    .build();

            repo.save(party);
            partyInfoRepository.saveAndFlush(partyInfo);
            return party.getId().toString();
        }catch (Exception e){

        }finally {
            redisLockRepository.unlock("party_create","party_create");
        }
        return null;
    }
    @Transactional
    public void updateParty(PartyForm partyForm){
        try {
            int count = 0;
            while(redisLockRepository.lock("party_update","update") && count < 10) {
                Thread.sleep(1000);
                count++;
            }
            if(count >= 10){
                throw new Exception();
            }
            Optional<Party> party = repo.findById(Long.parseLong(partyForm.getPartyId()));
            if (party.isPresent()) {
                Party updateparty = Party.updateParty(party.get(), partyForm);
                repo.saveAndFlush(updateparty);
            }
        }catch (Exception e) {

        }finally {
            redisLockRepository.unlock("party_update","update");
        }
    }
    public List<Party> findAllByUser(Users user) {
        return repo.findAllByUser(user);
    }
    public List<Party> findAllByUserAndState(Users user,String state){
        return repo.findAllByUserAndState(user,state);
    }

    @Transactional
    public String applyJoinParty(Party party, Users user, PartyInfoForm form){
        try {
            int count = 0;
            while(!redisLockRepository.lock("party_join","party_join") && count < 10){
                Thread.sleep(1000);
                count++;
            }
            if(count >= 10){
                throw new Exception();
            }
            if (party.getUser().getEmail().equals(user.getEmail())) {
                //파티장인경우
                return "host";
            }
            //기존 파티 정보가 있을 경우
            Optional<PartyInfo> optionalPartyInfo = partyInfoRepository.findByParty_IdAndUserEmail(party.getId(), user.getEmail());
            if (optionalPartyInfo.isPresent()) {
                PartyInfo partyInfo = optionalPartyInfo.get();
                //가입 거절 상태
                if (partyInfo.isBlack()) {
                    return "black";
                }
                //기존 정보 업데이트
                partyInfo = PartyInfo.updatePartyInfo(partyInfo, form);
                partyInfoRepository.saveAndFlush(partyInfo);
                return "success";
            }
            //이외 가입 신청 하기
            PartyInfo partyInfo = PartyInfo.builder()
                    .party(party)
                    .applicant_id(user)
                    .state("W")
                    .isBlack(false)
                    .build();


            partyInfoRepository.saveAndFlush(partyInfo);
            return "success";
        }catch (Exception e){

        }finally {
            redisLockRepository.unlock("party_join","party_join");
        }
        return "Fail";
    }
}
