package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.dto.PartyInfoForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Remove;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PartyInfoService {
    private final PartyInfoRepository repo;
    private final UsersRepository usersRepository;
    private final PartyRepository partyRepository;

    public Optional<PartyInfo> findByPartyId(Long PartyId){
        return repo.findByParty_id(PartyId);
    }
    public Optional<PartyInfo> findByPartIdAndUser(Long partyId, String email){
        Party party = partyRepository.findById(partyId).orElse(null);
        if(party == null || email == null){
            return Optional.empty();
        }
        Optional<PartyInfo> info = repo.findByParty_IdAndUserEmailAndState(partyId,email,"Y");
        System.err.println("info = " + info);
        return info;
    }
    public List<PartyInfo> findAllByPartyId(Party party){
        return repo.findAllByParty(party);
    }

    public Optional<PartyInfo> findByPartyAndUser(Party party,Users user){
        return repo.findByPartyAndUser(party,user);
    }

    // 현재 파티 참여인원 위한 값
    public List<PartyInfo> findAllPartyInfo(){
        return repo.findAll();
    }

    @Transactional
    public String changeStatus(Users user , Long partyInfoId, PartyInfoForm form){
        Optional<PartyInfo> optionalPartyInfo = repo.findById(partyInfoId);
        if(!optionalPartyInfo.isPresent())return "no partyInfo";
        PartyInfo partyInfo = optionalPartyInfo.get();
        Party party = partyInfo.getParty();
        //파티장 외 불가
        if(!user.getEmail().equals(party.getUser().getEmail()))return "no host";
        //파티장은 arkb 불가
        if(partyInfo.getUser().getEmail().equals(user.getEmail()))return "host";

        partyInfo = PartyInfo.updatePartyInfo(partyInfo , form);

        repo.saveAndFlush(partyInfo);
        return "ok";
    }
}
