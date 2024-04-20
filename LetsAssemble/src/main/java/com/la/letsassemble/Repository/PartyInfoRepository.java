package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyInfoRepository extends JpaRepository<PartyInfo,Long> {
    public Optional<PartyInfo> findByParty_id(Long Party_id);
    public List<PartyInfo> findByParty(Party party);
    Optional<PartyInfo> findByParty_IdAndUserEmailAndState(Long party_Id,String email,String state);
    Optional<PartyInfo> findByPartyAndUser(Party party, Users user);
    Optional<PartyInfo> findByParty_IdAndUserEmail(Long party_Id,String email);
    List<PartyInfo> findAll();
    List<PartyInfo> findAllByParty(Party party);
}
