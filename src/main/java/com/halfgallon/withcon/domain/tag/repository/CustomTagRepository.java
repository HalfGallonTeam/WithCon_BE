package com.halfgallon.withcon.domain.tag.repository;

import com.halfgallon.withcon.domain.tag.dto.TagCountDto;
import java.util.List;

public interface CustomTagRepository {

  List<TagCountDto> findTagOrderByCount();

  List<TagCountDto> findTagNameOrderByCount(String name, String performanceId);
}
