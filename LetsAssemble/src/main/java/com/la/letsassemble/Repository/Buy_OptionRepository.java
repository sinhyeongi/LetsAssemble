package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Buy_Option;
import com.la.letsassemble.QueryDsl.Buy_OptionCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Buy_OptionRepository extends JpaRepository<Buy_Option,Long>, Buy_OptionCustomRepository {
}
