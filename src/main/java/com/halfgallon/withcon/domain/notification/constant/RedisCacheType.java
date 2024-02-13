package com.halfgallon.withcon.domain.notification.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisCacheType {

  NOTIFICATION_CACHE("NotificationCache:"),

  VISIBLE_CACHE("VisibleCache:");

  private final String description;
}
