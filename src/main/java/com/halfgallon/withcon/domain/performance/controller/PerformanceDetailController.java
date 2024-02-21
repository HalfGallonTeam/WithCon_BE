package com.halfgallon.withcon.domain.performance.controller;

import com.halfgallon.withcon.domain.performance.dto.request.PerformanceDetailRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceDetailResponse;
import com.halfgallon.withcon.domain.performance.service.PerformanceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/performanceDetail")
@RequiredArgsConstructor
public class PerformanceDetailController {

  private final PerformanceDetailService performanceDetailService;

  @GetMapping("/{performanceDetailId}")
  public ResponseEntity<PerformanceDetailResponse> findPerformanceDetail(@PathVariable String performanceDetailId) {
    return ResponseEntity.ok(performanceDetailService.findPerformanceDetail(performanceDetailId));
  }

  @PutMapping
  public ResponseEntity<PerformanceDetailResponse> updatePerformanceDetail(@RequestBody PerformanceDetailRequest request) {
    return ResponseEntity.ok(performanceDetailService.updatePerformanceDetail(request));
  }
}
