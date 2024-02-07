package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.service.RedisNotificationService;
import com.halfgallon.withcon.domain.notification.service.handler.RedisListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisNotificationServiceImpl implements RedisNotificationService {

  private final RedisMessageListenerContainer container;
  private final RedisListener subscriber;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void subscribe(String channel) {
    container.addMessageListener(subscriber, ChannelTopic.of(channel));
    log.info("redis 채널 구독 성공");
  }

  @Override
  public void unsubscribe(String channel) {
    container.removeMessageListener(subscriber, ChannelTopic.of(channel));
    log.info("redis 채널 구독 해지");
  }

  // 특정 채널에 메세지 발행
  @Override
  public void publish(String channel, NotificationResponse notificationResponse) {
      redisTemplate.convertAndSend(channel, notificationResponse);
      log.info("redis 메세지 발행 성공");
  }
}
