package com.springboot.yogijogii.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SmsCertification {
    private final String PREFIX = "sms : ";
    private final int LIMIT_TIME = 3*60;
    private final StringRedisTemplate stringRedisTemplate;

    //레디스에 저장
    public void createSmsCertification(String phoneNum,String certification) {
        stringRedisTemplate.opsForValue().set(PREFIX+phoneNum,certification, Duration.ofSeconds(LIMIT_TIME));
    }

    //폰 번호에 해당하는 인증번호 불러오기
    public String getSmsCertification(String phoneNum) {
        return stringRedisTemplate.opsForValue().get(PREFIX+phoneNum);
    }

    public void deleteSmsCertification(String phoneNum) {
        stringRedisTemplate.delete(PREFIX+phoneNum);
    }
    public boolean haskey(String phoneNum) {
        return stringRedisTemplate.hasKey(PREFIX+phoneNum);
    }
}
