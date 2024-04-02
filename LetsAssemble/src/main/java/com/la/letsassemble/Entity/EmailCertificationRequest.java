package com.la.letsassemble.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class EmailCertificationRequest {
    @NotNull

    //@NotBlank(message = "이메일 입력은 필수입니다.")
    //@Email(message = "이메일 형식에 맞게 입력해 주세요.")
    private String email;

    //@Pattern(regexp="^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message="이메일 주소 양식을 확인해주세요")


}