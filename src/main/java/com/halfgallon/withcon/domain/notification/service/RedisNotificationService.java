package com.halfgallon.withcon.domain.notification.service;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;

public interface RedisNotificationService {

  // 채널 구독
  void subscribe(String channel);

  // 구독 해지
  void unsubscribe(String channel);

  // 메세지 발행
  void publish(String channel, NotificationResponse notificationResponse);

}
