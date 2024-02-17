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
  public void updateTagCount(String id, String name, Integer tagCount) {
    Map<String, Object> map = Map.of("name", name, "tag_count", tagCount);
    Document document = operations.getElasticsearchConverter().mapObject(map);

    UpdateQuery updateQuery = UpdateQuery.builder(id)
        .withDocument(document)
        .build();

    operations.update(updateQuery, operations.getIndexCoordinatesFor(TagSearch.class));
  }

}
