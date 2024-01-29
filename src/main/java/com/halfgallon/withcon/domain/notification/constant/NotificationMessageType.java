package com.halfgallon.withcon.domain.notification.constant;

import lombok.Getter;

@Getter
public enum NotificationMessageType {

  SUBSCRIBE("SSE 구독 완료");

  private final String description;

  NotificationMessageType(String description) {
    this.description = description;
  }

}
