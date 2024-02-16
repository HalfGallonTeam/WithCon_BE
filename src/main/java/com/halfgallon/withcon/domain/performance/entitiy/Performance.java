package com.halfgallon.withcon.domain.performance.entitiy;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.performance.constant.Status;
import com.halfgallon.withcon.domain.performance.dto.request.PerformanceRequest;
import com.halfgallon.withcon.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance extends BaseTimeEntity {

  @Id
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @Column(nullable = false)
  private String poster;

  @Column(nullable = false)
  private String facility;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(nullable = false) 
  private Long likes;

  @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChatRoom> chatRoom;

  @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default private List<PerformanceLike> performanceLikes = new ArrayList<>();

  @OneToOne(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
  private PerformanceDetail performanceDetail;

  public void update(PerformanceRequest request) {
    this.id = request.getId();
    this.name = request.getName();
    this.startDate = request.getStartDate();
    this.endDate = request.getEndDate();
    this.poster = request.getPoster();
    this.facility = request.getFacility();
    this.status = request.getStatus();
  }

  public void addLikes() {
    this.likes = this.likes + 1L;
  }

  public void subLikes() {
    this.likes = this.likes -1L;
  }
}
