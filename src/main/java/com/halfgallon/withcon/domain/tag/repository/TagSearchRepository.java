package com.halfgallon.withcon.domain.tag.repository;

import com.halfgallon.withcon.domain.tag.entity.TagSearch;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagSearchRepository extends ElasticsearchRepository<TagSearch, String>,
    CustomTagSearchRepository {
  Optional<TagSearch> findByNameAndPerformanceId(String name, String PerformanceId);
}
