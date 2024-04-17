package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party,Long> {
    List<Party> findAllByUser(Users user);
    @Query("SELECT pi.party FROM PartyInfo pi WHERE pi.user = :user AND pi.state = :state")
    List<Party> findAllByUserAndState(Users user,String state);
}
