package com.halfgallon.withcon.domain.performance.repository;

import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPerformanceRepository {
  Page<Performance> searchByKeyword(String keyword, Pageable pageable);
  Page<Performance> searchByKeywordAndGenre(String keyword, String genre, Pageable pageable);

}
