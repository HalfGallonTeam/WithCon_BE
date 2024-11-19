package com.halfgallon.withcon.domain.tag.dto;

import com.halfgallon.withcon.domain.tag.entity.TagSearch;
import lombok.Builder;

@Builder
public record TagSearchDto(
    String performanceId,
    String tagName,
    Integer count
) {
  public static TagSearchDto fromEntity(TagSearch tagSearch) {
    return TagSearchDto.builder()
        .performanceId(tagSearch.getPerformanceId())
        .tagName(tagSearch.getName())
        .count(tagSearch.getTagCount())
        .build();
  }
}
