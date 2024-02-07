package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class performanceNotificationRequest {
  private Long performanceId;
  private Member receiver;
  private String message;
  private NotificationType notificationType;
  private LocalDateTime createdAt;


}
