package com.halfgallon.withcon.domain.performance.repository;

import com.halfgallon.withcon.domain.performance.constant.Genre;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPerformanceRepository {
  List<Performance> findBestByPerformance(Genre genre, int size);

  Page<Performance> searchByKeyword(String keyword, Pageable pageable);
  
  Page<Performance> searchByKeywordAndGenre(String keyword, Genre genre, Pageable pageable);
}
