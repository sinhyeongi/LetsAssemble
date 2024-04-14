package com.la.letsassemble.dto;

import lombok.Data;

@Data
public class PartyForm {
    private String partyId; //update 시 사용
    private String category;
    private String isOnline;
    private String capacity;
    private String name;
    private String address;
    private String notification; //update 시 사용
    private String content;//update 시 사용
}
