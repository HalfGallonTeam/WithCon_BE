package com.halfgallon.withcon.domain.performance.entitiy;

import com.halfgallon.withcon.domain.performance.constant.Genre;
import com.halfgallon.withcon.domain.performance.dto.request.PerformanceDetailRequest;
import com.halfgallon.withcon.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PerformanceDetail extends BaseTimeEntity {

  @Id
  private String id;

  @Column(nullable = false)
  private Long price;

  @Column(nullable = false)
  private String actors;

  @Column(nullable = false)
  private String company;

  @Column(nullable = false)
  private String run_time;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Genre genre;

  @Column(nullable = false)
  private String time;

  @Column(nullable = false)
  private String age;

  @Column(nullable = false)
  private Double lat;

  @Column(nullable = false)
  private Double lot;

  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private Performance performance;

  public void update(PerformanceDetailRequest request) {
    this.id = request.getId();
    this.price = request.getPrice();
    this.actors = request.getActors();
    this.company = request.getCompany();
    this.run_time = request.getRun_time();
    this.genre = request.getGenre();
    this.time = request.getTime();
    this.age = request.getAge();
    this.lat = request.getLat();
    this.lot = request.getLot();
  }
}
