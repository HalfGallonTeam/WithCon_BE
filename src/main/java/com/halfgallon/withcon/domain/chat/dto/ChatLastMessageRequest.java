package com.halfgallon.withcon.domain.chat.dto;

import org.springframework.util.ObjectUtils;

public record ChatLastMessageRequest(
    Long lastMsgId,
    Integer limit
) {

  public ChatLastMessageRequest {
    if (ObjectUtils.isEmpty(limit) || limit == 0) {
      limit = 0;
    }
  }

}
