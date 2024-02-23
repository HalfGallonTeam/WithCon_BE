package com.halfgallon.withcon.domain.tag.service;

import com.halfgallon.withcon.domain.tag.dto.TagCountDto;
import com.halfgallon.withcon.domain.tag.dto.TagSearchDto;
import java.util.List;

public interface TagService {

  List<TagCountDto> findTagOrderByCount();

  List<TagCountDto> findTagNameOrderByCount(String tagName);

  List<TagSearchDto> findTagKeyword(String performanceId, String keyword);
}
