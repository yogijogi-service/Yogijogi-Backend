package com.springboot.yogijogii.data.repository.refreshToken.Impl;

import com.springboot.yogijogii.data.repository.refreshToken.RefreshTokenRepository;
import com.springboot.yogijogii.refreshToken.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RedisTemplate redisTemplate;

    @Override
    public void save(RefreshToken refreshToken) {
        // opsForValue -> 스트링을 위한 것.
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();

        // 만약 이미 유저네임이 존재하면 업데이트를 위한 기존 유저네임 삭제
        if(!Objects.isNull(valueOperations.get(refreshToken.getEmail()))){
            redisTemplate.delete(refreshToken.getEmail());
            log.info("refreshToken Repository save Update -> 업데이트를 위한 기존 key 삭제");
        }

        // 레디스에 키-값 저장
        valueOperations.set(refreshToken.getEmail(),refreshToken.getRefreshToken());
        // 만료 시간 24시간
        redisTemplate.expire(refreshToken.getEmail(),60 * 60 * 24, TimeUnit.SECONDS);

    }

    @Override
    public Optional<RefreshToken> findByUsername(String username) {
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(username);

        if(refreshToken == null){
            return Optional.empty();
        }else{
            return Optional.of(new RefreshToken(username,refreshToken));
        }
    }

    @Override
    public void delete(String username) {
        redisTemplate.delete(username);
    }
}
