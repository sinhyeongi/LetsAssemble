package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Buy_Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface Buy_OptionRepository extends JpaRepository<Buy_Option,Long>, Buy_OptionCustomRepository {
}
