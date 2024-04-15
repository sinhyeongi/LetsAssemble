package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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

}
