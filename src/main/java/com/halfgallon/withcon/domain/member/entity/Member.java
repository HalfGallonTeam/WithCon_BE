package com.halfgallon.withcon.domain.member.entity;

import static lombok.AccessLevel.PROTECTED;

import com.halfgallon.withcon.domain.member.constant.GenderType;
import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private LoginType loginType;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private GenderType genderType;

  @Column(nullable = false)
  private String phoneNumber;

}
