package com.halfgallon.withcon.domain.performance.dto.response;

import com.halfgallon.withcon.domain.performance.constant.Status;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PerformanceResponse {

  private String id;

  private String name;

  private String startDate;

  private String endDate;

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
