package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.notification.constant.VisibleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisibleRequest {

  @NotNull
  private Long performanceId;

  @NotNull
  private Long chatRoomId;

  @NotNull
  @Enumerated(value = EnumType.STRING)
  private VisibleType visibleType;
}
