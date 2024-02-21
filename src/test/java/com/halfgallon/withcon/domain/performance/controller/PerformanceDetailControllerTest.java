package com.halfgallon.withcon.domain.performance.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.performance.constant.Genre;
import com.halfgallon.withcon.domain.performance.dto.request.PerformanceDetailRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceDetailResponse;
import com.halfgallon.withcon.domain.performance.service.PerformanceDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(PerformanceDetailController.class)
class PerformanceDetailControllerTest {

  @MockBean
  PerformanceDetailService performanceDetailService;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  WebApplicationContext webApplicationContext;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .build();
  }


  @Test
  @DisplayName("공연 세부 정보 조회 완료")
  void findPerformanceDetail_Success() throws Exception {
    String id = "id";

    given(performanceDetailService.findPerformanceDetail(id)).willReturn(getPerformanceDetailResponse());

    mockMvc.perform(get("/performanceDetail/" + id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("공연 세부 정보 수정 완료")
  void updatePerformanceDetail_Success() throws Exception {
  given(performanceDetailService.updatePerformanceDetail(getPerformanceDetailRequest())).willReturn(getPerformanceDetailResponse());

  mockMvc.perform(put("/performanceDetail")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(getPerformanceDetailResponse())))
      .andExpect(status().isOk())
      .andDo(print());
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