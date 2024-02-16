package com.halfgallon.withcon.domain.performance.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genre {
  ALL("전체"),
  MUSICAL("뮤지컬"),
  CONCERT("콘서트"),
  THEATER("연극");

  private final String description;
}
