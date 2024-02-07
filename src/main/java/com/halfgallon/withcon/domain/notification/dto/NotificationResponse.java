package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.notification.entity.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponse {
  private Long memberId;

  private String message;

  private String url;

  private boolean readStatus;

  public NotificationResponse(Notification notification) {
    this.memberId = notification.getMember().getId();
    this.message = notification.getMessage();
    this.url = notification.getUrl();
    this.readStatus = notification.isReadStatus();
  }
}
