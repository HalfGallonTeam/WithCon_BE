package com.halfgallon.withcon.domain.performance.repository.impl;

import static com.halfgallon.withcon.domain.performance.entitiy.QPerformance.performance;
import static com.halfgallon.withcon.domain.performance.entitiy.QPerformanceDetail.performanceDetail;

import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.repository.CustomPerformanceRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomPerformanceRepositoryImpl implements CustomPerformanceRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Performance> searchPerformance(String keyword) {
    return jpaQueryFactory
        .selectFrom(performance)
        .leftJoin(performance.performanceDetail, performanceDetail)
        .where(performance.name.contains(keyword)
            .or(performanceDetail.actors.contains(keyword)))
        .distinct()
        .fetch();
  }
}
