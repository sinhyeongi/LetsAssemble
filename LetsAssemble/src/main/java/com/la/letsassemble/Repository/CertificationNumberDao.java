package com.la.letsassemble.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
@Repository
@RequiredArgsConstructor
public class CertificationNumberDao {

    private final StringRedisTemplate redisTemplate;

    public void saveCertificationNumber(String email, String certificationNumber) {
        redisTemplate.opsForValue()
                .set(email, certificationNumber,
                        //Duration.ofSeconds(EMAIL_VERIFICATION_LIMIT_IN_SECONDS));
                        Duration.ofSeconds(180));
    }

    public String getCertificationNumber(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void removeCertificationNumber(String email) {
        redisTemplate.delete(email);
    }

    public boolean hasKey(String email) {
        Boolean keyExists = redisTemplate.hasKey(email);
        return keyExists != null && keyExists;
    }
}