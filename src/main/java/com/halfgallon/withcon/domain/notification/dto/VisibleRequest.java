package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.notification.constant.VisibleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisibleRequest {

  @NotNull
  private Long chatRoomId;

  @NotNull
  private VisibleType visibleType;
}
