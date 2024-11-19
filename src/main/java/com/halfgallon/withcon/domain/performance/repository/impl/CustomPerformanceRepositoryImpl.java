package com.halfgallon.withcon.domain.performance.repository.impl;

import static com.halfgallon.withcon.domain.performance.entitiy.QPerformance.performance;
import static com.halfgallon.withcon.domain.performance.entitiy.QPerformanceDetail.performanceDetail;

import com.halfgallon.withcon.domain.performance.constant.Genre;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.repository.CustomPerformanceRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CustomPerformanceRepositoryImpl implements CustomPerformanceRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<Performance> searchByKeyword(String keyword, Pageable pageable) {
    QueryResults<Performance> results = jpaQueryFactory
        .selectFrom(performance)
        .leftJoin(performance.performanceDetail, performanceDetail)
        .where(performance.name.contains(keyword)
            .or(performanceDetail.actors.contains(keyword)))
        .distinct()
        .orderBy(performance.createdAt.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
  }

  @Override
  public Page<Performance> searchByKeywordAndGenre(String keyword, Genre genre, Pageable pageable) {
    QueryResults<Performance> results = jpaQueryFactory
        .selectFrom(performance)
        .leftJoin(performance.performanceDetail, performanceDetail)
        .where(performance.name.contains(keyword)
            .or(performanceDetail.actors.contains(keyword)))
        .where(performanceDetail.genre.eq(genre))
        .distinct()
        .orderBy(performance.createdAt.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
  }

  public List<Performance> findBestByPerformance(Genre genre, int size) {
    return jpaQueryFactory.select(performance)
        .from(performanceDetail)
        .join(performanceDetail.performance, performance)
        .where(performanceDetail.genre.eq(genre))
        .orderBy(performance.likes.desc())
        .limit(size)
        .fetch();
  }
}
