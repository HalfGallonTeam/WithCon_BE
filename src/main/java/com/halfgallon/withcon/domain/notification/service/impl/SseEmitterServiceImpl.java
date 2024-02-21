package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.repository.SseEmitterRepository;
import com.halfgallon.withcon.domain.notification.service.RedisCacheService;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import java.io.IOException;
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

    emitters.forEach((key, value) -> send(value, key, notificationResponse));
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
}