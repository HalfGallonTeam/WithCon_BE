package com.halfgallon.withcon.domain.performance.repository;

import com.halfgallon.withcon.domain.performance.entitiy.PerformanceLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceLikeRepository extends JpaRepository<PerformanceLike, Long> {

  boolean existsByMember_IdAndPerformance_Id(Long memberId, String performanceId);

  Optional<PerformanceLike> findByMember_idAndPerformance_Id(
      Long memberId, String performanceId);

  void deleteByMember_IdAndPerformance_Id(Long memberId, String performanceId);

  List<PerformanceLike> findPerformanceLikeByMember_Id(Long memberId);

}
