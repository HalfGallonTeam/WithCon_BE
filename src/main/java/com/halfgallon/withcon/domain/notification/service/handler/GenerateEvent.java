package com.halfgallon.withcon.domain.notification.service.handler;

import com.halfgallon.withcon.domain.notification.event.NewMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateEvent {

  private final ApplicationEventPublisher eventPublisher;

  public void doSomething(Long performanceId, Long chatRoomId) {
    this.eventPublisher.publishEvent(
        new NewMessageEvent(this, performanceId,chatRoomId));
    log.info("Event 발행");
  }
}
