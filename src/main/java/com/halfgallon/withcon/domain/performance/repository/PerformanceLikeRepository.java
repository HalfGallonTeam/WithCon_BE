package com.halfgallon.withcon.domain.performance.repository;

import com.halfgallon.withcon.domain.performance.entitiy.PerformanceLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceLikeRepository extends JpaRepository<PerformanceLike, Long> {

}
