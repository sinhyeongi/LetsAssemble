package com.la.letsassemble.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserForm {
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String phone;
    private String gender;
    private int age;
    private String provider; // 소셜타입
    private String providerId;
}
