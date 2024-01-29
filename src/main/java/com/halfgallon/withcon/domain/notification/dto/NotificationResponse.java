package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.notification.entity.Notification;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

  private String message;

  private String url;

  private boolean isRead;

  private LocalDateTime createdAt;

  public NotificationResponse(Notification notification) {
    this.message = notification.getMessage();
    this.url = notification.getUrl();
    this.isRead = notification.isRead();
    this.createdAt = notification.getCreatedAt();
  }
}
