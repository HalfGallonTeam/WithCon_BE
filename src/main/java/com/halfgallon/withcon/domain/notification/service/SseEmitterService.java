package com.halfgallon.withcon.domain.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterService {

  SseEmitter subscribe(Long memberId, String lastEventId);

  void send(SseEmitter sseEmitter, String emitterId, Object data);

}
