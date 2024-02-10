package com.halfgallon.withcon.domain.performance.controller;

import com.halfgallon.withcon.domain.performance.dto.request.PerformanceRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import com.halfgallon.withcon.domain.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performance")
public class PerformanceController {

  private final PerformanceService performanceService;

  @PostMapping
  public ResponseEntity<PerformanceResponse> createPerformance(@RequestBody PerformanceRequest request) {
    return ResponseEntity.ok(performanceService.createPerformance(request));
  }

  @GetMapping("/{performanceId}")
  public ResponseEntity<PerformanceResponse> findPerformance(@PathVariable String performanceId) {
    return ResponseEntity.ok(performanceService.findPerformance(performanceId));
  }

  @PutMapping
  public ResponseEntity<PerformanceResponse> updatePerformance(@RequestBody PerformanceRequest request) {
    return ResponseEntity.ok(performanceService.updatePerformance(request));
  }

  @DeleteMapping("/{performanceId}")
  public ResponseEntity<PerformanceResponse> deletePerformance(@PathVariable String performanceId) {
    return ResponseEntity.ok(performanceService.deletePerformance(performanceId));
  }
}
