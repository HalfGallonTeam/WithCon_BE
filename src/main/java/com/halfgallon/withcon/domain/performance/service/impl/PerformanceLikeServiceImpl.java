package com.halfgallon.withcon.domain.performance.service.impl;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.performance.constant.Genre;
import com.halfgallon.withcon.domain.performance.dto.response.MainPagePerformanceResponse;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.entitiy.PerformanceLike;
import com.halfgallon.withcon.domain.performance.repository.PerformanceLikeRepository;
import com.halfgallon.withcon.domain.performance.repository.PerformanceRepository;
import com.halfgallon.withcon.domain.performance.service.PerformanceLikeService;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformanceLikeServiceImpl implements PerformanceLikeService {

  private final PerformanceLikeRepository performanceLikeRepository;
  private final MemberRepository memberRepository;
  private final PerformanceRepository performanceRepository;

  // 장르별 베스트 5개씩 조회
  @Override
  @Transactional(readOnly = true)
  public MainPagePerformanceResponse bestPerformance(String genre, int size) {
    String[] category = genre.split(",");

    List<Integer> parts = new ArrayList<>();

    List<PerformanceResponse> responses = Arrays.stream(category)
        .flatMap(value -> {
          List<Performance> performances = performanceRepository
              .findBestByPerformance(Genre.valueOf(value), size);
          parts.add(performances.size());
          return performances.stream();
        })
        .map(PerformanceResponse::fromEntity)
        .toList();

    return MainPagePerformanceResponse.builder()
        .performanceResponses(responses)
        .parts(parts)
        .build();
  }

  // 찜 하기
  @Override
  @Transactional
  public String likePerformance(Long memberId, String performanceId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Performance performance = performanceRepository.findById(performanceId)
        .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));

    boolean exist = performanceLikeRepository
        .existsByMember_IdAndPerformance_Id(memberId, performanceId);

    if(!exist) {
      performance.addLikes();

      PerformanceLike performanceLike = PerformanceLike.builder()
          .member(member)
          .performance(performance)
          .build();
      performanceLikeRepository.save(performanceLike);
      log.info("Service : 공연 찜 성공");

      performance.getPerformanceLikes().add(performanceLike);
      member.getLikes().add(performanceLike);

      return performanceId;
    }
    throw new CustomException(ErrorCode.ALREADY_LIKE_EXIST);
  }

  // 찜 해제
  @Override
  @Transactional
  public String unlikePerformance(Long memberId, String performanceId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Performance performance = performanceRepository.findById(performanceId)
        .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));

    PerformanceLike performanceLike = performanceLikeRepository
        .findByMember_IdAndPerformance_Id(memberId, performanceId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_LIKE));

    performance.subLikes();

    performanceLikeRepository.deletePerformanceLikeById(performanceLike.getId());
    log.info("Service : 공연 찜 해제");

    performance.getPerformanceLikes().remove(performanceLike);
    member.getLikes().remove(performanceLike);

    return performanceId;
  }

  // 나의 찜 공연 목록
  @Override
  @Transactional(readOnly = true)
  public Page<PerformanceResponse> findLikes(Long memberId, Pageable pageable) {
    Page<PerformanceLike> performanceLikes = performanceLikeRepository.
        findPerformanceLikeByMember_Id(memberId, pageable);

    log.info("Service : 찜 목록 조회");
    return performanceLikes.map(
        performanceLike -> PerformanceResponse.fromEntity(performanceLike.getPerformance()));
  }

  // 나의 찜 공연Id 목록
  @Override
  @Transactional(readOnly = true)
  public List<String> findLikesId(Long memberId) {
    List<PerformanceLike> performanceLikes = performanceLikeRepository.
        findPerformanceLikeByMember_Id(memberId);

    log.info("Service : 찜 목록 조회");
    return performanceLikes.stream()
        .map(performanceLike -> performanceLike.getPerformance().getId())
        .collect(Collectors.toList());
  }
}
