package com.halfgallon.withcon.domain.performance.repository;

import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import java.util.List;

public interface CustomPerformanceRepository {

  List<Performance> searchPerformance(String keyword);

}
