package com.la.letsassemble.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserForm {
    String email;
    String password;
    String nickname;
}
