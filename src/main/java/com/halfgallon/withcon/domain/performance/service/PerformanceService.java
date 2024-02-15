package com.halfgallon.withcon.domain.performance.service;

import com.halfgallon.withcon.domain.performance.dto.request.PerformanceRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformanceService {

  PerformanceResponse createPerformance(PerformanceRequest request);
  PerformanceResponse findPerformance(String performanceId);
  PerformanceResponse updatePerformance(PerformanceRequest request);
  PerformanceResponse deletePerformance(String performanceId);
  Page<PerformanceResponse> searchPerformance(String keyword, Pageable pageable);
}
