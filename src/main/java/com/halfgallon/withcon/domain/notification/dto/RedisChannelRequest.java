package com.halfgallon.withcon.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisChannelRequest {

  private Long performanceId;

  private Long chatRoomId;

}
