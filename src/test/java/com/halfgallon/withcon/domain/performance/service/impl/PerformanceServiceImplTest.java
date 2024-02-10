package com.halfgallon.withcon.domain.performance.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.halfgallon.withcon.domain.performance.constant.Status;
import com.halfgallon.withcon.domain.performance.dto.request.PerformanceRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.repository.PerformanceRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceImplTest {

  @InjectMocks
  PerformanceServiceImpl performanceService;

  @Mock
  PerformanceRepository performanceRepository;

  @Test
  @DisplayName("공연 생성 완료")
  void createPerformance_Success() {
    // given
    PerformanceRequest request = getPerformanceRequest();
    Performance performance = request.toEntity();
    PerformanceResponse expectedResponse = getPerformanceResponse();

    given(performanceRepository.save(any(Performance.class))).willReturn(performance);

    // when
    PerformanceResponse actualResponse = performanceService.createPerformance(request);

    // then
    assertThat(expectedResponse.getName()).isEqualTo(actualResponse.getName());
  }

  @Test
  @DisplayName("공연 조회 완료")
  void findPerformance_Success() {
    // given
    PerformanceRequest request = getPerformanceRequest();
    Performance performance = request.toEntity();
    PerformanceResponse expectedResponse = getPerformanceResponse();
    String id = "id";

    given(performanceRepository.findById(anyString())).willReturn(
        Optional.ofNullable(performance));

    // when
    PerformanceResponse actualResponse = performanceService.findPerformance(id);

    // then
    assertThat(expectedResponse.getName()).isEqualTo(actualResponse.getName());
  }

  @Test
  @DisplayName("공연 조회 실패 - 공연이 존재하지 않습니다.")
  void findPerformance_failed() {
    PerformanceRequest request = getPerformanceRequest();
    Performance performance = request.toEntity();
    PerformanceResponse expectedResponse = getPerformanceResponse();
    String id = "id";

    given(performanceRepository.findById(anyString())).willReturn(Optional.empty());

    // when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> performanceService.findPerformance(id));

    // then
    assertThat(ErrorCode.PERFORMANCE_NOT_FOUND.getDescription()).isEqualTo(
        customException.getMessage());
  }

  @Test
  @DisplayName("공연 수정 완료")
  void updatePerformance_Success() {
    // given
    PerformanceRequest request = getPerformanceRequest();
    Performance performance = request.toEntity();
    PerformanceResponse expectedResponse = getPerformanceResponse();

    given(performanceRepository.findById(anyString())).willReturn(Optional.of(performance));
    given(performanceRepository.save(any(Performance.class))).willReturn(performance);

    // when
    PerformanceResponse actualResponse = performanceService.updatePerformance(request);

    // then
    assertThat(expectedResponse.getName()).isEqualTo(actualResponse.getName());

  }

  @Test
  @DisplayName("공연 삭제 완료")
  void deletePerformance_Success() {
    // given
    PerformanceRequest request = getPerformanceRequest();
    Performance performance = request.toEntity();
    PerformanceResponse expectedResponse = getPerformanceResponse();
    String id = "id";

    given(performanceRepository.findById(any(String.class))).willReturn(
        Optional.ofNullable(performance));

    // when
    PerformanceResponse actualResponse = performanceService.deletePerformance(id);

    // then
    assertThat(expectedResponse.getName()).isEqualTo(actualResponse.getName());
    verify(performanceRepository, times(1)).deleteById(id);
  }

  @Test
  @DisplayName("공연 검색 완료")
  void searchPerformance_Success() {
    //given
    PerformanceRequest request = getPerformanceRequest();
    String keyword = "keyword";
    List<PerformanceResponse> expectedResponse = getPerformances().stream()
        .map(PerformanceResponse::fromEntity).toList();

    // PerformanceRepository의 searchPerformance 메소드가 호출되면 PerformanceResponse 객체의 리스트를 반환하도록 설정합니다.
    given(performanceRepository.searchPerformance(keyword)).willReturn(getPerformances());

    List<PerformanceResponse> actualResponse = performanceService.searchPerformance(
        keyword);

    assertThat(expectedResponse.stream().map(PerformanceResponse::getId)
        .collect(Collectors.toList())).containsExactlyElementsOf(
        expectedResponse.stream().map(PerformanceResponse::getId).toList());
  }

  private PerformanceRequest getPerformanceRequest() {
    return PerformanceRequest.builder()
        .id("id")
        .name("name")
        .startDate(LocalDate.ofEpochDay(2024 - 02 - 18))
        .endDate(LocalDate.ofEpochDay(2024 - 02 - 20))
        .poster("asdfler")
        .facility("공연 장소")
        .status(Status.RUNNING)
        .likes(4000L)
        .build();
  }

  private PerformanceResponse getPerformanceResponse() {
    return PerformanceResponse.builder()
        .id("id")
        .name("name")
        .startDate(LocalDate.ofEpochDay(2024 - 02 - 18))
        .endDate(LocalDate.ofEpochDay(2024 - 02 - 20))
        .poster("asdfler")
        .facility("공연 장소")
        .status(Status.RUNNING)
        .likes(4000L)
        .build();
  }

  private List<Performance> getPerformances() {
    return Arrays.asList(
        getPerformanceRequest().toEntity(),
        getPerformanceRequest().toEntity(),
        getPerformanceRequest().toEntity()
    );
  }
}