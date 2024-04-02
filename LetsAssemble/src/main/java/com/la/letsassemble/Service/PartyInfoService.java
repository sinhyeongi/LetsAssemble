package com.la.letsassemble.Service;

import com.la.letsassemble.Repository.PartyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyInfoService {
    private final PartyInfoRepository repo;

}
