package com.halfgallon.withcon.domain.performance.entitiy;

import com.halfgallon.withcon.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
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
  private String genre;

  @Column(nullable = false)
  private String sty;

  @Column(nullable = false)
  private String time;

  @Column(nullable = false)
  private String age;

  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private Performance performance;
}
