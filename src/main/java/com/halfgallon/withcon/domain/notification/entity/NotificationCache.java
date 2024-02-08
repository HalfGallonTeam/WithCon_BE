package com.halfgallon.withcon.domain.notification.entity;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "NotificationCache", timeToLive = 60 * 60) // 1시간
public class NotificationCache {

  @Id
  private Long memberId; // 해시 키

  private Map<String, NotificationResponse> notifications; // emitterId 는 Key
}
