package com.halfgallon.withcon.domain.performance.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.halfgallon.withcon.domain.performance.constant.Genre;
import com.halfgallon.withcon.domain.performance.dto.request.PerformanceDetailRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceDetailResponse;
import com.halfgallon.withcon.domain.performance.entitiy.PerformanceDetail;
import com.halfgallon.withcon.domain.performance.repository.PerformanceDetailRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PerformanceDetailServiceImplTest {

  @InjectMocks
  PerformanceDetailServiceImpl performanceDetailService;

  @Mock
  PerformanceDetailRepository performanceDetailRepository;

  @Test
  @DisplayName("공연 세부 내용 조회 완료")
  void findPerformanceDetail_Success() {
    PerformanceDetailRequest performanceDetailRequest = getPerformanceDetailRequest();
    PerformanceDetail performanceDetail = performanceDetailRequest.toEntity();
    PerformanceDetailResponse expectedResponse = getPerformanceDetailResponse();
    String id = "id";

    given(performanceDetailRepository.findById(id)).willReturn(
        Optional.ofNullable(performanceDetail));

    PerformanceDetailResponse actualResponse = performanceDetailService.findPerformanceDetail(id);

    assertThat(actualResponse.getId()).isEqualTo(expectedResponse.getId());

  }

  private PerformanceDetailRequest getPerformanceDetailRequest() {
    return PerformanceDetailRequest.builder()
        .id("id")
        .price(10000L)
        .actors("배우 이름")
        .company("회사 이름")
        .run_time("100분")
        .genre(Genre.CONCERT)
        .time("공연 시간")
        .age("나이 제한")
        .lat(37.124)
        .lot(127.3241)
        .build();
  }

  private PerformanceDetailResponse getPerformanceDetailResponse() {
    return PerformanceDetailResponse.builder()
        .id("id")
        .price(10000L)
        .actors("배우 이름")
        .company("회사 이름")
        .run_time("100분")
        .genre(Genre.CONCERT)
        .time("공연 시간")
        .age("나이 제한")
        .lat(37.124)
        .lot(127.3241)
        .build();
  }

}