package com.halfgallon.withcon.domain.notification.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
  CHATROOM("/chatRoom"),
  PERFORMANCE("/performance");

  private final String description;
}
