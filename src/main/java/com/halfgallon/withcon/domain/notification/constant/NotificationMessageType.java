package com.halfgallon.withcon.domain.notification.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationMessageType {

  SUBSCRIBE("SSE 구독 완료");

  private final String description;

}
