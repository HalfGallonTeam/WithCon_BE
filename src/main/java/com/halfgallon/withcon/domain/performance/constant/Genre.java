package com.halfgallon.withcon.domain.performance.constant;

import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genre {
  MUSICAL("뮤지컬"),
  CONCERT("콘서트"),
  THEATER("연극");

  private final String description;

  public static Genre fromDescription(String description) {
    return Arrays.stream(Genre.values())
        .filter(genre ->  genre.getDescription().equals(description))
        .findFirst()
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PARAMETER));
  }
}
