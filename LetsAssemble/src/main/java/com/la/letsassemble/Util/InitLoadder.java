package com.la.letsassemble.Util;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InitLoadder implements CommandLineRunner {

    private final UsersRepository repo;
    private final BCryptPasswordEncoder encoder;
    public InitLoadder(UsersRepository repository, BCryptPasswordEncoder encoder){
        repo = repository;
        this.encoder = encoder;
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
        Optional<Users> user= repo.findByEmail(u.getEmail());
        if(user.isPresent()){
            System.out.println("user = " + user.get());
        }

    }
}
