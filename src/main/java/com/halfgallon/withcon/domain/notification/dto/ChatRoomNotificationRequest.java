package com.halfgallon.withcon.domain.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
  @Pattern(regexp = "^(입장|퇴장|강퇴)$")
  private String chatRoomGenerateType;

}
