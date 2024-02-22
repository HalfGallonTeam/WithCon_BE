package com.halfgallon.withcon.domain.performance.service.impl;

import com.halfgallon.withcon.domain.performance.dto.request.PerformanceDetailRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceDetailResponse;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.entitiy.PerformanceDetail;
import com.halfgallon.withcon.domain.performance.repository.PerformanceDetailRepository;
import com.halfgallon.withcon.domain.performance.repository.PerformanceRepository;
import com.halfgallon.withcon.domain.performance.service.PerformanceDetailService;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceDetailServiceImpl implements PerformanceDetailService {

  private final PerformanceDetailRepository performanceDetailRepository;
  private final PerformanceRepository performanceRepository;

  @Override
  public PerformanceDetailResponse findPerformanceDetail(String performanceId) {
    return PerformanceDetailResponse.fromEntity(
        performanceDetailRepository.findById(performanceId).orElseThrow(() -> new CustomException(
            ErrorCode.PERFORMANCE_DETAIL_NOT_FOUND)));
  }

  @Override
  public PerformanceDetailResponse updatePerformanceDetail(
      PerformanceDetailRequest performanceDetailRequest) {
    PerformanceDetail performanceDetail = performanceDetailRepository.findById(
            performanceDetailRequest.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));

    performanceDetail.update(performanceDetailRequest);

    return PerformanceDetailResponse.fromEntity(
        performanceDetailRepository.save(performanceDetail));
  }

}
