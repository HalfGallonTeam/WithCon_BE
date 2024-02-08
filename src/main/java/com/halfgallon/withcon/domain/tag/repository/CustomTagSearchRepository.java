package com.halfgallon.withcon.domain.tag.repository;

public interface CustomTagSearchRepository {
  void updateTagCount(String id, String name, Integer tagCount);
}
