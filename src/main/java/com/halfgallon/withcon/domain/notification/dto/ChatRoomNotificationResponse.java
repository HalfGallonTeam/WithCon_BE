package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomNotificationResponse {
  private Long chatRoomId;
  private Long performanceId;
  private Member receiver;
  private String url;
  private String message;
  private NotificationType notificationType;
  boolean isRead;
  private LocalDateTime createdAt;
}
