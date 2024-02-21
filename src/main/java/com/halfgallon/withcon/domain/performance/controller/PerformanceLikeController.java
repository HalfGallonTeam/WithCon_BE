package com.halfgallon.withcon.domain.performance.controller;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.performance.dto.response.MainPagePerformanceResponse;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import com.halfgallon.withcon.domain.performance.service.PerformanceLikeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/performance")
@RequiredArgsConstructor
public class PerformanceLikeController {

  private final PerformanceLikeService performanceLikeService;

  // 장르별 찜 많은순 5개 조회
  @GetMapping("/best")
  public ResponseEntity<MainPagePerformanceResponse> bestPerformance(
      @RequestParam String genre,
      @RequestParam int size) {

    return ResponseEntity.ok(
        performanceLikeService.bestPerformance(genre, size));
  }

  // 찜 하기
  @PostMapping("/{performanceId}/like")
  public ResponseEntity<String> likePerformance(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable String performanceId) {

    return ResponseEntity.ok(performanceLikeService.likePerformance(
        customUserDetails.getId(), performanceId));
  }

  // 찜 해제
  @DeleteMapping("/{performanceId}/unlike")
  public ResponseEntity<String> unlikePerformance(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable String performanceId) {

    return ResponseEntity.ok(performanceLikeService.unlikePerformance(
        customUserDetails.getId(), performanceId));
  }

  @GetMapping("/favorite")
  public ResponseEntity<Page<PerformanceResponse>> findLikes(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PageableDefault() Pageable pageable) {

    return ResponseEntity.ok(
        performanceLikeService.findLikes(customUserDetails.getId(), pageable));
  }

  // 나의 찜 공연 id 목록
  @GetMapping("/favorite-id")
  public ResponseEntity<List<String>> findLikesId(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return ResponseEntity.ok(
        performanceLikeService.findLikesId(customUserDetails.getId()));
  }
}