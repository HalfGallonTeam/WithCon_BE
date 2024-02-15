package com.halfgallon.withcon.domain.performance.dto.request;

import com.halfgallon.withcon.domain.performance.constant.Status;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PerformanceRequest {

  private String id;

  private String name;

  private LocalDate startDate;

  private LocalDate endDate;

  private String poster;

  private String facility;

  private Status status;

  public Performance toEntity() {
    return Performance.builder()
        .id(id)
        .name(name)
        .startDate(startDate)
        .endDate(endDate)
        .poster(poster)
        .facility(facility)
        .status(status)
        .build();
  }
}
