package com.springboot.yogijogii.refreshToken;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Indexed;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "refreshToken", timeToLive = 60*60*24)
public class RefreshToken {
    @Id
    private String email;

    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
