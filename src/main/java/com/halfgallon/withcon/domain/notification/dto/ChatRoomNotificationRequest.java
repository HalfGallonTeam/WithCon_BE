package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomNotificationRequest {

  @NotNull
  private Long chatRoomId;

  @NotNull
  private Long targetId; // 입장/퇴장/강퇴의 대상이 된 사람

  @NotNull
  @Enumerated(value = EnumType.STRING)
  private MessageType messageType;

}
