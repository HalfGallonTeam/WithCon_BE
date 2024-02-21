package com.halfgallon.withcon.domain.notification.service;


import com.halfgallon.withcon.domain.notification.dto.ChatRoomNotificationRequest;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


public interface NotificationService {

  SseEmitter subscribe(Long memberId);

  List<NotificationResponse> findNotification(Long memberId);

  void createNotificationChatRoom(ChatRoomNotificationRequest request);

  void readNotification(Long notificationId);

}
