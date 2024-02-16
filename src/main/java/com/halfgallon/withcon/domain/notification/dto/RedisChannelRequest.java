package com.halfgallon.withcon.domain.notification.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisChannelRequest {

  @NotNull
  private Long performanceId;

  @NotNull
  private Long chatRoomId;

}
