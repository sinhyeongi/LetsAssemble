package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.dto.PartyForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository repo;
    public Optional<Party> findByPartyId(Long id){
        return repo.findById(id);
    }

    public Party createParty(PartyForm from, Users user){

        Party party = new Party().builder()
                        .interest(from.getCategory()) //관심사
                        .title(from.getName()) //파티이름
                        .user(user)// 파티장
                        .isOnline(from.getIsOnline() == "online")//온라인 여부
                        .area(from.getAddress())
                        .personnel(Integer.parseInt(from.getCapacity()))
                        .build();
        return repo.save(party);
    }

    public void updateParty(PartyForm partyForm){
        Optional<Party> party = repo.findById(Long.parseLong(partyForm.getPartyId()));
        if(party.isPresent()){
            Party updateparty = Party.updateParty(party.get(),partyForm);
            repo.save(updateparty);
        }
    }
}
