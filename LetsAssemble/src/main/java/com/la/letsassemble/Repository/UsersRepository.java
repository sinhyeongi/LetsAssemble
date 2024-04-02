package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsersRepository extends JpaRepository<Users,Long> {

}
