package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.repository.SseEmitterRepository;
import com.halfgallon.withcon.domain.notification.service.RedisCacheService;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterServiceImpl implements SseEmitterService {

  private final RedisCacheService redisCacheService;
  private final SseEmitterRepository sseEmitterRepository;

  @Override
  public void sendNotificationToClient(NotificationResponse notificationResponse) {
    Map<String, SseEmitter> emitters =
        sseEmitterRepository.findAllByMemberIdStartsWith(
            notificationResponse.getMemberId().toString());

    emitters.forEach((key, value) -> {
      saveNotificationCache(key, notificationResponse);
      log.info("Service 캐시 저장");
      send(value, key, notificationResponse);
    });
  }

  // 알림 전송
  @Override
  public void send(SseEmitter sseEmitter, String emitterId, Object data) {
    try {
      sseEmitter.send(SseEmitter.event()
          .id(emitterId)
          .data(data, MediaType.APPLICATION_JSON));
          log.info("알림 데이터 전송 성공");
    } catch (IOException e) {
      log.info("전송 실패");
      sseEmitterRepository.deleteById(emitterId);
    }
  }

  private void saveNotificationCache(String emitterId,NotificationResponse response) {
    String hashKey = RedisCacheType.NOTIFICATION_CACHE.getDescription()
        + response.getMemberId();

    Map<Object, Object> cache = redisCacheService.getHashByKey(hashKey);
    log.info("cache " + cache);

    if(cache != null) { // Map이 있다면
      redisCacheService.updateToHash(hashKey, emitterId, response);
      log.info("Service : 이미 존재하는 member key : 저장 성공 " + response.getMemberId());
    }else {
      Map<Object, Object> newObject = new HashMap<>();
      newObject.put(emitterId, response);
      redisCacheService.saveToHash(hashKey, newObject, 1);
      log.info("Service : 새로운 member key : 저장 성공 " + response.getMemberId());
    }
  }
}