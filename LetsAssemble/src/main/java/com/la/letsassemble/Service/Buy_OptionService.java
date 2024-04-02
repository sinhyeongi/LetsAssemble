package com.la.letsassemble.Service;

import com.la.letsassemble.Repository.Buy_OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Buy_OptionService {
    private final Buy_OptionRepository repo;
}
