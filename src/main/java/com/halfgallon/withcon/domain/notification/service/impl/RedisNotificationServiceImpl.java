package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.dto.VisibleDataDto;
import com.halfgallon.withcon.domain.notification.dto.VisibleRequest;
import com.halfgallon.withcon.domain.notification.service.RedisCacheService;
import com.halfgallon.withcon.domain.notification.service.RedisNotificationService;
import com.halfgallon.withcon.domain.notification.service.handler.RedisListener;
import java.util.HashMap;
import java.util.Map;
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
  private final RedisCacheService redisCacheService;

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

  // Redis에 visible 데이터 저장.
  @Override
  public void createVisibleCache(Long memberId, VisibleRequest request) {
    String hashKey = RedisCacheType.VISIBLE_CACHE.getDescription() + request.getChatRoomId();
    log.info("VisibleCache 채널 명 : " + hashKey);

    Map<Object, Object> visibleCaches = redisCacheService.getHashByKey(hashKey);
    log.info("Service : 특정 채팅방에 대한 Visible Map 조회 성공" + visibleCaches);

    VisibleDataDto visibleDataDto = new VisibleDataDto(request);

    if (!visibleCaches.isEmpty()) { // 특정 채팅방의 Map이 이미 존재한다면
      redisCacheService.updateToHash(hashKey, String.valueOf(memberId), visibleDataDto);
      log.info("Redis Visible 기존 데이터 변경" + memberId);
    } else {
      Map<Object, Object> newObject = new HashMap<>();
      newObject.put(String.valueOf(memberId), visibleDataDto);
      redisCacheService.saveToHash(hashKey, newObject, 24);
      log.info("Redis Visible 새로운 멤버 추가 " + memberId);
    }
  }
}


