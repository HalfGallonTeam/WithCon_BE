package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.constant.NotificationMessageType;
import com.halfgallon.withcon.domain.notification.repository.SseEmitterRepository;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterServiceImpl implements SseEmitterService {
  private static final Long TIME_OUT = 5L * 1000 * 60;

  private final SseEmitterRepository sseEmitterRepository;

  @Override
  public SseEmitter subscribe(String username, String lastEventId) {
    String emitterId = createEmitterId(username);
    SseEmitter sseEmitter = sseEmitterRepository.save(emitterId, new SseEmitter(TIME_OUT));

    // 더미 데이터(503 에러 방지)
    send(sseEmitter, emitterId, NotificationMessageType.SUBSCRIBE + " username: " + username);
    log.info("SSE 구독 완료");

    sseEmitter.onCompletion(() -> sseEmitterRepository.deleteById(emitterId));
    sseEmitter.onError((e) -> sseEmitter.complete());
    sseEmitter.onTimeout(() -> sseEmitterRepository.deleteById(emitterId));

    return sseEmitter;
  }

  private String createEmitterId(String username) {
    return username + "_" + System.currentTimeMillis();
  }

  @Override
  public void send(SseEmitter sseEmitter, String emitterId, Object data) {
    try {
      sseEmitter.send(SseEmitter.event()
          .id(emitterId)
          .data(data));
    }catch (IOException e) {
      log.info("전송 실패");
      sseEmitterRepository.deleteById(emitterId);
    }
  }
}
