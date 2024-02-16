package com.halfgallon.withcon.domain.performance.service;

import com.halfgallon.withcon.domain.performance.dto.response.MainPagePerformanceResponse;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import java.util.List;

public interface PerformanceLikeService {

  MainPagePerformanceResponse bestPerformance(String category, int size);

  String likePerformance(Long memberId, String performanceId);

  String unlikePerformance(Long memberId, String performanceId);

  List<PerformanceResponse> findLikes(Long memberId);

  List<String> findLikesId(Long memberId);

}
