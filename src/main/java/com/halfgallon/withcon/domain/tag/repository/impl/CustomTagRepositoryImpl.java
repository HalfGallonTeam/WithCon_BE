package com.halfgallon.withcon.domain.tag.repository.impl;

import static com.halfgallon.withcon.domain.tag.entity.QTag.tag;

import com.halfgallon.withcon.domain.tag.dto.TagCountDto;
import com.halfgallon.withcon.domain.tag.repository.CustomTagRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomTagRepositoryImpl implements CustomTagRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<TagCountDto> findTagOrderByCount() {
    return jpaQueryFactory.select(
            Projections.fields(TagCountDto.class,
                tag.name,
                tag.count().as("count"),
                tag.performance.name.as("performance")))
        .from(tag)
        .orderBy(tag.count().desc())
        .groupBy(tag.name, tag.performance.name)
        .limit(10)  //인기 태그 Top 10
        .fetch();
  }

  @Override
  public List<TagCountDto> findTagNameOrderByCount(String name, String performanceId) {
    return jpaQueryFactory.select(
            Projections.fields(TagCountDto.class,
                tag.name,
                tag.count().as("count"),
                tag.performance.name.as("performance")))
        .from(tag)
        .where(tag.name.startsWithIgnoreCase(name), tag.performance.id.eq(performanceId))
        .groupBy(tag.name)
        .orderBy(tag.count().desc())
        .limit(10)
        .fetch();
  }

}
