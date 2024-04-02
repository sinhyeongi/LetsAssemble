package com.la.letsassemble.Service;

import com.la.letsassemble.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository repo;
    private final BCryptPasswordEncoder encoder;
}
