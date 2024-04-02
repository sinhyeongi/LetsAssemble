package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.PartyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyInfoRepository extends JpaRepository<PartyInfo,Long> {
}
