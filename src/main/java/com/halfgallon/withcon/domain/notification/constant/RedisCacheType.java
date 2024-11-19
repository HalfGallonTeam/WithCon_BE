package com.halfgallon.withcon.domain.notification.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisCacheType {

  VISIBLE_CACHE("visibleCache:");

  private final String description;
}
