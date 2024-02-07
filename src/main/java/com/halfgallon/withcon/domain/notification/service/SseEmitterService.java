package com.halfgallon.withcon.domain.notification.service;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterService {

  void sendNotificationToClient(NotificationResponse notificationResponse);
  void send(SseEmitter sseEmitter, String emitterId, Object data);

}
