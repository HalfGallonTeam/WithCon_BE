package com.halfgallon.withcon.domain.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterService {

  SseEmitter subscribe(String username, String lastId);

  void send(SseEmitter sseEmitter, String emitterId, Object data);

}
