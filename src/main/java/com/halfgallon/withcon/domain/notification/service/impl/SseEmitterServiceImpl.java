package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.entity.NotificationCache;
import com.halfgallon.withcon.domain.notification.repository.NotificationCacheRepository;
import com.halfgallon.withcon.domain.notification.repository.SseEmitterRepository;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterServiceImpl implements SseEmitterService {

  private final SseEmitterRepository sseEmitterRepository;
  private final NotificationCacheRepository notificationCacheRepository;

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
    Optional<NotificationCache> cache = notificationCacheRepository.findByMemberId(
        response.getMemberId());
    if(cache.isPresent()) { // 값이 이미 있다면
      NotificationCache existCache = cache.get();
      existCache.getNotifications().put(emitterId, response);
      notificationCacheRepository.save(existCache);
      log.info("Service : 이미 존재하는 member key : 저장 성공 " + existCache.getMemberId());
    }else {
      NotificationCache newCache = NotificationCache.builder()
          .memberId(response.getMemberId())
          .notifications(new HashMap<>() {{
            put(emitterId, response);
          }})
          .build();
      notificationCacheRepository.save(newCache);
      log.info("Service : 새로운 member key : 저장 성공 " + newCache.getMemberId());
    }
  }
}