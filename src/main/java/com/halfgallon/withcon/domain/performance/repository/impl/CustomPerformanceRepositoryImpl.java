package com.halfgallon.withcon.domain.performance.repository.impl;

import static com.halfgallon.withcon.domain.performance.entitiy.QPerformance.performance;
import static com.halfgallon.withcon.domain.performance.entitiy.QPerformanceDetail.performanceDetail;

import com.halfgallon.withcon.domain.performance.constant.Genre;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.repository.CustomPerformanceRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
  public Page<Performance> searchByKeywordAndGenre(String keyword, String genre, Pageable pageable) {
    QueryResults<Performance> results = jpaQueryFactory
        .selectFrom(performance)
        .leftJoin(performance.performanceDetail, performanceDetail)
        .where(performance.name.contains(keyword)
            .or(performanceDetail.actors.contains(keyword)))
        .where(performanceDetail.genre.eq(Genre.valueOf(genre))) // genre와 일치하는 Performance만 추가적으로 필터링합니다.
        .distinct()
        .orderBy(performance.createdAt.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
  }
}
