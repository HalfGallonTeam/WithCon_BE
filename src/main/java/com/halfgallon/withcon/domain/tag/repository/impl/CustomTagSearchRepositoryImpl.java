package com.halfgallon.withcon.domain.tag.repository.impl;

import com.halfgallon.withcon.domain.tag.entity.TagSearch;
import com.halfgallon.withcon.domain.tag.repository.CustomTagSearchRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

@RequiredArgsConstructor
public class CustomTagSearchRepositoryImpl implements CustomTagSearchRepository {

  private final ElasticsearchOperations operations;

  @Override
  public void updateSearchTag(TagSearch tagSearch, Integer tagCount) {
    Map<String, Object> map = Map.of("name", tagSearch.getName(),
        "performance_id", tagSearch.getPerformanceId(),
        "tag_count", tagCount);

    UpdateQuery updateQuery = UpdateQuery.builder(tagSearch.getId())
        .withDocument(operations.getElasticsearchConverter().mapObject(map))
        .build();

    operations.update(updateQuery, operations.getIndexCoordinatesFor(TagSearch.class));
  }

}
