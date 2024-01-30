package com.halfgallon.withcon.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halfgallon.withcon.domain.chat.constant.MessageType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto {
  private Long memberId;
  private Long roomId;

  private String message;
  private MessageType messageType;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime time;
}
