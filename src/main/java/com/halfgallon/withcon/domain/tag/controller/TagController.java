package com.halfgallon.withcon.domain.tag.controller;

import com.halfgallon.withcon.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;

  /**
   * 태그 정보 조회(태그 갯수 많은 순으로 정렬)
   * @return : tagName(태그 이름), count(태그 생성 갯수)
   */
  @GetMapping("/search")
  public ResponseEntity<?> findTagOrderByCount() {
    return ResponseEntity.ok(tagService.findTagOrderByCount());
  }

  /**
   * 공연에 따른 태그 이름 검색
   * @return : tagName(태그 이름), count(태그 생성 갯수), performanceId(공연 ID)
   */
  @GetMapping("/{tagName}/search/performance/{performanceId}")
  public ResponseEntity<?> findTagNameOrderByCount(
      @PathVariable("tagName") String tagName,
      @PathVariable("performanceId") String performanceId) {
    return ResponseEntity.ok(tagService.findTagNameOrderByCount(tagName, performanceId));
  }

  /**
   * 태그 검색(ElasticSearch)
   */
  @GetMapping("/search/autocomplete")
  public ResponseEntity<?> findTagKeyword(
      @RequestParam("performance") String performanceId,
      @RequestParam("keyword") String keyword) {
    return ResponseEntity.ok(tagService.findTagKeyword(performanceId, keyword));
  }

}
