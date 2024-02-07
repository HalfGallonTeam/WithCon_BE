package com.halfgallon.withcon.domain.performance.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import com.halfgallon.withcon.domain.performance.constant.Status;
import com.halfgallon.withcon.domain.performance.dto.request.PerformanceRequest;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import com.halfgallon.withcon.domain.performance.service.impl.PerformanceServiceImpl;
import com.halfgallon.withcon.global.annotation.WithCustomMockUser;
import java.time.LocalDate;
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

@WebMvcTest(PerformanceController.class)
class PerformanceControllerTest {

  @MockBean
  PerformanceServiceImpl performanceService;

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
  @WithCustomMockUser
  @DisplayName("공연 생성 완료")
  void createPerformance_Success() throws Exception{

    given(performanceService.createPerformance(getPerformanceRequest())).willReturn(getPerformanceResponse());


    mockMvc.perform(post("/performance")

        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(getPerformanceRequest())))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("공연 조회 완료")
  void findPerformance_Success() throws Exception {
    String id = "id";

    given(performanceService.createPerformance(getPerformanceRequest())).willReturn(getPerformanceResponse());


    mockMvc.perform(get("/performance/" + id)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andDo(print());
  }

  @Test
  @DisplayName("공연 수정 완료")
  void updatePerformance_Success() throws Exception {

    given(performanceService.createPerformance(getPerformanceRequest())).willReturn(getPerformanceResponse());

    mockMvc.perform(put("/performance")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(getPerformanceRequest())))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("공연 삭제 완료")
  void deletePerformance_Success() throws Exception {
    String id = "id";

    given(performanceService.createPerformance(getPerformanceRequest())).willReturn(getPerformanceResponse());

    mockMvc.perform(delete("/performance/" + id)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  private static PerformanceRequest getPerformanceRequest() {
    return PerformanceRequest.builder()
        .id("id")
        .name("name")
        .startDate(LocalDate.ofEpochDay(2024-02-18))
        .endDate(LocalDate.ofEpochDay(2024-02-20))
        .poster("asdfler")
        .facility("공연 장소")
        .status(Status.RUNNING)
        .likes(4000L)
        .build();
  }

  private static PerformanceResponse getPerformanceResponse() {
    return PerformanceResponse.builder()
        .id("id")
        .name("name")
        .startDate(LocalDate.ofEpochDay(2024-02-18))
        .endDate(LocalDate.ofEpochDay(2024-02-20))
        .poster("asdfler")
        .facility("공연 장소")
        .status(Status.RUNNING)
        .likes(4000L)
        .build();
  }

}