package com.halfgallon.withcon.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "accessToken", timeToLive = 60 * 60 * 3) // 3시간
public class AccessToken {

  @Id
  private Long memberId;

  @Indexed
  private String accessToken;

}
