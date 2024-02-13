package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.notification.constant.VisibleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisibleRequest {

  @NotBlank
  private Long performanceId;

  @NotBlank
  private Long chatRoomId;

  @NotBlank
  @Enumerated(value = EnumType.STRING)
  private VisibleType visibleType;
}
