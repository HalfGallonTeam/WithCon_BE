package com.halfgallon.withcon.domain.performance.dto.response;

import com.halfgallon.withcon.domain.performance.constant.Status;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PerformanceResponse {

  private String id;

  private String name;

  private LocalDate startDate;

  private LocalDate endDate;

  private String poster;

  private String facility;

  private Status status;

  private Long likes;

  public static PerformanceResponse fromEntity(Performance performance) {
    return PerformanceResponse.builder()
        .id(performance.getId())
        .name(performance.getName())
        .startDate(performance.getStartDate())
        .endDate(performance.getEndDate())
        .poster(performance.getPoster())
        .facility(performance.getFacility())
        .status(performance.getStatus())
        .likes(performance.getLikes())
        .build();
  }
}
