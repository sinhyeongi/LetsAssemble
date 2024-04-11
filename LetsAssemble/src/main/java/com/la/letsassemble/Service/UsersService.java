package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.dto.UserForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder encoder;

    public Optional<Users> findByEmail (String email){
        return usersRepository.findByEmail(email);
    }

    public Optional<Users> findByProviderAndProviderId(String provider, String providerId){
        return usersRepository.findByProviderAndProviderId(provider,providerId);
    }

    public Optional<Users>findByNickName(String nickname){
        return usersRepository.findByNickname(nickname);
    }
    @Transactional
    public Users signup(UserForm form){
        Users user = Users.createUser(form,encoder);
        System.out.println("signup user = " + user.toString());
        if(user != null){
            return usersRepository.saveAndFlush(user);
        }
        return null;
    }
    public void UpdatePricipal(Users u){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof PricipalDetails) {
            PricipalDetails details = (PricipalDetails) authentication.getPrincipal();
            details.setUser(u);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(details,authentication.getCredentials(),details.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

}
