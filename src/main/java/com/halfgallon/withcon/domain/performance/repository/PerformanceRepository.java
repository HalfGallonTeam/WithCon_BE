package com.halfgallon.withcon.domain.performance.repository;

import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, String>,
    CustomPerformanceRepository {

  List<Performance> findAllByStartDate(LocalDate date);
}
