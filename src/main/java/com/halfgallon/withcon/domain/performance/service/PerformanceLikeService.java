package com.halfgallon.withcon.domain.performance.service;

import com.halfgallon.withcon.domain.performance.dto.response.MainPagePerformanceResponse;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformanceLikeService {

  MainPagePerformanceResponse bestPerformance(String genre, int size);

  String likePerformance(Long memberId, String performanceId);

  String unlikePerformance(Long memberId, String performanceId);
  Page<PerformanceResponse> findLikes(Long memberId, Pageable pageable);

  List<String> findLikesId(Long memberId);

}
