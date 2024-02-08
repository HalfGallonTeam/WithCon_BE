package com.halfgallon.withcon.domain.member.entity;

import static lombok.AccessLevel.PROTECTED;

import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.domain.performance.entitiy.PerformanceLike;
import com.halfgallon.withcon.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column
  private String password;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private LoginType loginType;

  @Column(nullable = false)
  private String nickname;

  @Column
  private String phoneNumber;

  @OneToMany
  private List<PerformanceLike> likes;
}
