package com.la.letsassemble.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Component
@Slf4j
public class CertificationGenerator {
    public String createCertificationNumber() throws NoSuchAlgorithmException {
        String result;

        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new NoSuchAlgorithmException("인증번호 생성중 에러 발생");
        }
    }
}
