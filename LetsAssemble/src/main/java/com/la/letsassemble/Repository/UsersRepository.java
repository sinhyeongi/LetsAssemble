package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    public Optional<Users> findByEmail(String email);
    Optional<Users> findByProviderAndProviderId(String provider,String providerId);
}
