package com.halfgallon.withcon.domain.performance.service.impl;

import static org.mockito.BDDMockito.given;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.entitiy.PerformanceLike;
import com.halfgallon.withcon.domain.performance.repository.PerformanceLikeRepository;
import com.halfgallon.withcon.domain.performance.repository.PerformanceRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PerformanceLikeServiceImplTest {

  @InjectMocks
  private PerformanceLikeServiceImpl performanceLikeService;
  @Mock
  private PerformanceLikeRepository performanceLikeRepository;
  @Mock
  private MemberRepository memberRepository;
  @Mock
  private PerformanceRepository performanceRepository;

  @Test
  @DisplayName("찜 성공")
  void success_like_performance() {
    //given
    Member member = Member.builder()
        .id(1L)
        .likes(new ArrayList<>())
        .build();

    Long memberId = 1L;

    given(memberRepository.findById(memberId))
        .willReturn(Optional.of(member));

    Performance performance = Performance.builder()
        .id("FF1")
        .likes(5L)
        .performanceLikes(new ArrayList<>())
        .build();

    String performanceId = "FF1";

    given(performanceRepository.findById(performanceId))
        .willReturn(Optional.of(performance));

    given(performanceLikeRepository.existsByMember_IdAndPerformance_Id(memberId, performanceId))
        .willReturn(false);
    
    PerformanceLike performanceLike = PerformanceLike.builder()
        .id(3L)
        .member(member)
        .performance(performance)
        .build();

    member.getLikes().add(performanceLike);
    performance.getPerformanceLikes().add(performanceLike);

    //when
    String perId = performanceLikeService.likePerformance(memberId, performanceId);
    //then

    Assertions.assertThat(member.getLikes().get(0)).
        isEqualTo(performanceLike);
    Assertions.assertThat(performance.getPerformanceLikes().get(0)).
        isEqualTo(performanceLike);
    Assertions.assertThat(performance.getLikes()).isEqualTo(6L);
    Assertions.assertThat(perId).isEqualTo(performanceId);
  }

  @Test
  @DisplayName("찜 취소 성공")
  void success_unlike_performance() {
    //given
    Member member = Member.builder()
        .id(1L)
        .likes(new ArrayList<>())
        .build();

    Long memberId = 1L;

    given(memberRepository.findById(memberId))
        .willReturn(Optional.of(member));

    Performance performance = Performance.builder()
        .id("FF1")
        .likes(5L)
        .performanceLikes(new ArrayList<>())
        .build();

    String performanceId = "FF1";

    given(performanceRepository.findById(performanceId))
        .willReturn(Optional.of(performance));

    PerformanceLike performanceLike = PerformanceLike.builder()
        .id(3L)
        .member(member)
        .performance(performance)
        .build();

    given(performanceLikeRepository.findByMember_idAndPerformance_Id(
        memberId, performanceId))
        .willReturn(Optional.of(performanceLike));

    member.getLikes().add(performanceLike);
    performance.getPerformanceLikes().add(performanceLike);

    //when
    String perId = performanceLikeService.unlikePerformance(memberId, performanceId);
    //then

    Assertions.assertThat(member.getLikes().get(0)).
        isEqualTo(performanceLike);
    Assertions.assertThat(performance.getPerformanceLikes().get(0)).
        isEqualTo(performanceLike);
    Assertions.assertThat(performance.getLikes()).isEqualTo(4L);
    Assertions.assertThat(perId).isEqualTo(performanceId);
  }
}