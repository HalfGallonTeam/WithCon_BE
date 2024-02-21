package com.halfgallon.withcon.domain.performance.service;

import com.halfgallon.withcon.domain.performance.dto.request.PerformanceDetailRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceDetailResponse;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;

public interface PerformanceDetailService {

  PerformanceDetailResponse findPerformanceDetail(String performanceId);

  PerformanceDetailResponse updatePerformanceDetail(PerformanceDetailRequest performanceDetailReqeust);
}
