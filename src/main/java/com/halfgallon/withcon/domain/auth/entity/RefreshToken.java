package com.halfgallon.withcon.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14) // 2주
public class RefreshToken {

  @Id
  private Long memberId;

  @Indexed
  private String refreshToken;

}
