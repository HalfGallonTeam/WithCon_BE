package com.halfgallon.withcon.domain.performance.dto.request;

import com.halfgallon.withcon.domain.performance.constant.Status;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PerformanceRequest {

  private String id;

  private String name;

  private String startDate;

  private String endDate;

  private String poster;

  private String facility;

  private Status status;

  private Long likes;

  public Performance toEntity() {
    return Performance.builder()
        .id(id)
        .name(name)
        .startDate(startDate)
        .endDate(endDate)
        .poster(poster)
        .facility(facility)
        .status(status)
        .likes(likes)
        .build();
  }
}
