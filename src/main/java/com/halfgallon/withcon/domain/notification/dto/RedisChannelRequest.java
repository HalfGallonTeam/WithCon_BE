package com.halfgallon.withcon.domain.notification.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisChannelRequest {

  @NotBlank
  private Long performanceId;

  @NotBlank
  private Long chatRoomId;

}
