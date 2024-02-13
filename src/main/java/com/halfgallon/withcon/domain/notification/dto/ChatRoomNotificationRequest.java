package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomNotificationRequest {

  @NotBlank
  private Long chatRoomId;

  @NotBlank
  private Long performanceId;

  @NotBlank
  private Long targetId; // 입장/퇴장/강퇴의 대상이 된 사람

  @NotBlank
  @Enumerated(value = EnumType.STRING)
  private MessageType messageType;

}
