package com.halfgallon.withcon.domain.tag.repository;

import com.halfgallon.withcon.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, CustomTagRepository {
  Integer countTagByNameAndPerformance_Id(String name, String performanceId);
}
