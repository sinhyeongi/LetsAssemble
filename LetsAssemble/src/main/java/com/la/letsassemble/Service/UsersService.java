package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Security_Custom.PrincipalDetailsService;
import com.la.letsassemble.dto.PasswordForm;
import com.la.letsassemble.dto.UserForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public String createTemporaryPassword(String email){
        //유저 가져오기
        Optional<Users> u = usersRepository.findByEmail(email);
        Users user = u.get();
        //임시 비밀번호 생성
        String temporary = Users.generateRandomString(8);

        //임시비밀번호 주입
        user.builder().password(encoder.encode(temporary));
        return temporary;
    }

    @Transactional
    public String changePassword(Users user, PasswordForm form){
        if(user == null)return"not user error";
        if(form.getEmail() == "" || form.getPassword1() == "" || form.getPassword2() == "")return "empty error";
        if(!form.getPassword1().equals(form.getPassword2()))return "not equals password";
        if(form.getPassword1().length() < 8) return "not 8 characters";

        user.changePassword(user,encoder.encode(form.getPassword1()));

        usersRepository.saveAndFlush(user);
        return "ok";
    }
    @Transactional
    public ResponseEntity<String> changeNickname(PricipalDetails userDetails, UserForm form){
        Users user = userDetails.getUser();

        if(form.getNickname().length() < 3){
            return ResponseEntity.badRequest().body("not length");
        }
        if(!user.getEmail().equals(form.getEmail())){
            return ResponseEntity.badRequest().body("badRequest");
        }
        if(user.getNickname().equals(form.getNickname())){
            return ResponseEntity.badRequest().body("same");
        }
        if(usersRepository.findByNickname(form.getNickname()).isPresent()){
            return ResponseEntity.badRequest().body("not valid");
        }
        user.changeNickname(user,form.getNickname());
        usersRepository.saveAndFlush(user);
        userDetails.setUser(user);
        return ResponseEntity.ok().body("ok");
    }

    // 모든 유저 목록 출력
    public List<Users> findAllUsers(){
        List<Users> list = usersRepository.findAll();
        return list;
    }

    @Transactional
    public String deleteUser(Long id) {
        usersRepository.deleteById(id);

        return "ok";
    }
}
