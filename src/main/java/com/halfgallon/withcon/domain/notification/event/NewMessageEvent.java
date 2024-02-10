package com.halfgallon.withcon.domain.notification.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewMessageEvent extends ApplicationEvent {
  private final Long performanceId;
  private final Long chatRoomId;

  public NewMessageEvent(Object source, Long performanceId, Long chatRoomId) {
    super(source);
    this.performanceId = performanceId;
    this.chatRoomId = chatRoomId;
  }

}
