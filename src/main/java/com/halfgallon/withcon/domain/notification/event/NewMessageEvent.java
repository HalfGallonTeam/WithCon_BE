package com.halfgallon.withcon.domain.notification.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewMessageEvent extends ApplicationEvent {
  private final Long chatRoomId;

  public NewMessageEvent(Object source, Long chatRoomId) {
    super(source);
    this.chatRoomId = chatRoomId;
  }
}
