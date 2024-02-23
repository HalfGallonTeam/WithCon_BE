package com.halfgallon.withcon.domain.chat.dto;

import org.springframework.util.ObjectUtils;

public record ChatMessageRequest(
    Long lastMsgId,
    Integer limit
) {

  public ChatMessageRequest {
    if (ObjectUtils.isEmpty(limit) || limit == 0) {
      limit = 0;
    }
  }

}
