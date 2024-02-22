package com.halfgallon.withcon.domain.performance.dto.request;

import com.halfgallon.withcon.domain.performance.constant.Genre;
import com.halfgallon.withcon.domain.performance.entitiy.PerformanceDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PerformanceDetailRequest {
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

  public PerformanceDetail toEntity() {
    return PerformanceDetail.builder()
        .id(id)
        .price(price)
        .actors(actors)
        .company(company)
        .run_time(run_time)
        .genre(genre)
        .time(time)
        .age(age)
        .lat(lat)
        .lot(lot)
        .build();
  }
}
