package com.la.letsassemble.Service;

import com.la.letsassemble.Repository.MessageReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageReadService {
    private final MessageReadRepository repo;
}
