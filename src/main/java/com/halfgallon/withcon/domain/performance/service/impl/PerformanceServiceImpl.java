package com.halfgallon.withcon.domain.performance.service.impl;

import com.halfgallon.withcon.domain.performance.dto.request.PerformanceRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.repository.PerformanceRepository;
import com.halfgallon.withcon.domain.performance.service.PerformanceService;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {

  private final PerformanceRepository performanceRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  @Transactional
  public PerformanceResponse createPerformance(PerformanceRequest request) {
    return PerformanceResponse.fromEntity(performanceRepository.save(request.toEntity()));
  }

  @Override
  @Transactional(readOnly = true)
  public PerformanceResponse findPerformance(String performanceId) {
    return PerformanceResponse.fromEntity(
        performanceRepository.findById(performanceId).orElseThrow(() -> new CustomException(
            ErrorCode.PERFORMANCE_NOT_FOUND)));
  }

  @Override
  @Transactional
  public PerformanceResponse updatePerformance(PerformanceRequest request) {
    Performance performance = performanceRepository.findById(request.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));

    performance.update(request);
    return PerformanceResponse.fromEntity(performanceRepository.save(performance));
  }

  @Override
  @Transactional
  public PerformanceResponse deletePerformance(String performanceId) {
    Performance performance = performanceRepository.findById(performanceId)
        .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));

    performanceRepository.deleteById(performanceId);

    return PerformanceResponse.fromEntity(performance);
  }

  @Override
  public Page<PerformanceResponse> searchPerformance(String keyword, Pageable pageable) {
    Page<Performance> performancePage = performanceRepository.searchPerformance(keyword, pageable);
    List<PerformanceResponse> performanceResponseList = performancePage.getContent()
        .stream()
        .map(PerformanceResponse::fromEntity)
        .collect(Collectors.toList());

    return new PageImpl<>(performanceResponseList, pageable, performancePage.getTotalElements());
  }
}
