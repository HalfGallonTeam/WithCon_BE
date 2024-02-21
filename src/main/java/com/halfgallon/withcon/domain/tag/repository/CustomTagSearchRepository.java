package com.halfgallon.withcon.domain.tag.repository;

import com.halfgallon.withcon.domain.tag.entity.TagSearch;
import java.util.List;

public interface CustomTagSearchRepository {
  void updateSearchTag(TagSearch tagSearch, Integer tagCount);

  List<TagSearch> findTagAutoComplete(String performanceId, String name);

}
