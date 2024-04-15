package com.la.letsassemble.QueryDsl;

import com.la.letsassemble.Entity.Message;
import com.la.letsassemble.dto.MessageDTO;


import java.util.List;


public interface MessageCustomRepository {
    List<MessageDTO> findPartyIdAndLimit30(Long partyId);
}
