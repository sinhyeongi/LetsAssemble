package com.la.letsassemble.Util;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InitLoadder implements CommandLineRunner {

    private final UsersRepository repo;
    private final PartyRepository partyRepository;
    private final BCryptPasswordEncoder encoder;
    public InitLoadder(UsersRepository repository, BCryptPasswordEncoder encoder,PartyRepository partyRepository){
        repo = repository;
        this.encoder = encoder;
        this.partyRepository = partyRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        Users u = new Users().builder()
                .email("test@test.tes")
                .password(encoder.encode("test"))
                .phone("010-1234-1234")
                .name("test")
                .nickname("test")
                .gender("M")
                .age(22)
                .build();
        repo.saveAndFlush(u);

        Party party = new Party().builder()
                .isOnline(false)
                .personnel(100)
                .area("test")
                .content("test")
                .user(u)
                .interest("test")
                .notification("test")
                .title("test")
                .build();

        Optional<Users> user= repo.findByEmail(u.getEmail());
        if(user.isPresent()){
            System.out.println("party = "+partyRepository.save(party));
            System.out.println("user = " + user.get());
        }

    }
}
