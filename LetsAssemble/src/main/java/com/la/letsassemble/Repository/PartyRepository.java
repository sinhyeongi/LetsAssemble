package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party,Long> {
}
