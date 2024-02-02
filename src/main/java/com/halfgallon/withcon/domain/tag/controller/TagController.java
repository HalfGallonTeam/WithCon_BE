package com.halfgallon.withcon.domain.tag.controller;

import com.halfgallon.withcon.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;

  /**
   * 태그 정보 조회(태그 갯수 많은 순으로 정렬)
   * @return : name(태그 이름), count(태그 생성 갯수)
   */
  @GetMapping("/search")
  public ResponseEntity<?> findTagOrderByCount() {
    return ResponseEntity.ok(tagService.findTagOrderByCount());
  }

  /**
   * 태그 이름 검색 시에 태그 갯수가 많은 순으로 정렬
   * @return : name(태그 이름), count(태그 생성 갯수)
   */
  @GetMapping("/{tagName}/search")
  public ResponseEntity<?> findTagNameOrderByCount(@PathVariable("tagName") String tagName) {
    return ResponseEntity.ok(tagService.findTagNameOrderByCount(tagName));
  }

}
