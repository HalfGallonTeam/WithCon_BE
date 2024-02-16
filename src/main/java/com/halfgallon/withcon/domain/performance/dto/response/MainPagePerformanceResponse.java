package com.halfgallon.withcon.domain.performance.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainPagePerformanceResponse {

  private List<PerformanceResponse> performanceResponses;

  private List<Integer> parts;

}
