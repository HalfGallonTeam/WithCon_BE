package com.halfgallon.withcon.domain.tag.dto;

import com.halfgallon.withcon.domain.tag.entity.Tag;
import lombok.Builder;

@Builder
public record TagDto(
    String name
) {
  public static TagDto fromEntity(Tag tag) {
    return TagDto.builder()
        .name(tag.getName())
        .build();
  }
}
