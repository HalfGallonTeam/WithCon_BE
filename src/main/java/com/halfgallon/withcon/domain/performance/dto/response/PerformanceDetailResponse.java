package com.halfgallon.withcon.domain.performance.dto.response;

import com.halfgallon.withcon.domain.performance.constant.Genre;
import com.halfgallon.withcon.domain.performance.entitiy.PerformanceDetail;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PerformanceDetailResponse {

  private String id;

  private Long price;

  private String actors;

  private String company;

  private String run_time;

  private Genre genre;

  private String time;

  private String age;

  private Double lat;

  private Double lot;

  public static PerformanceDetailResponse fromEntity(PerformanceDetail performanceDetail) {
    return PerformanceDetailResponse.builder()
        .id(performanceDetail.getId())
        .price(performanceDetail.getPrice())
        .actors(performanceDetail.getActors())
        .company(performanceDetail.getCompany())
        .run_time(performanceDetail.getRun_time())
        .genre(performanceDetail.getGenre())
        .time(performanceDetail.getTime())
        .age(performanceDetail.getAge())
        .lat(performanceDetail.getLat())
        .lot(performanceDetail.getLot())
        .build();
  }
}
