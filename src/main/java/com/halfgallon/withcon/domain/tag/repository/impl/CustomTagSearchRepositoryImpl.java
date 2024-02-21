package com.halfgallon.withcon.domain.tag.repository.impl;

import com.halfgallon.withcon.domain.tag.entity.TagSearch;
import com.halfgallon.withcon.domain.tag.repository.CustomTagSearchRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
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

  @Override
  public List<TagSearch> findTagAutoComplete(String performanceId, String name) {
    CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria());
    criteriaQuery.addCriteria(Criteria.where("performance_id").is(performanceId));
    criteriaQuery.addCriteria(Criteria.where("name").startsWith(name));

    Sort sort = Sort.by(Sort.Order.desc("tag_count"));
    criteriaQuery.addSort(sort);

    SearchHits<TagSearch> hits = operations.search(criteriaQuery, TagSearch.class);
    return hits.stream().map(SearchHit::getContent).limit(10).toList();
  }


}
