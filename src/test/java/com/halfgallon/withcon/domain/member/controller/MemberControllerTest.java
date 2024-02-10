package com.halfgallon.withcon.domain.member.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.member.dto.request.UpdateMemberRequest;
import com.halfgallon.withcon.domain.member.service.MemberService;
import com.halfgallon.withcon.global.annotation.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MemberService memberService;

  @Autowired
  WebApplicationContext webApplicationContext;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .build();
  }

  @Test
  @DisplayName("내 정보 조회")
  @WithCustomMockUser
  void findMyInformation() throws Exception {
    // given
    CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    Long memberId = principal.getId();

    // when
    mockMvc.perform(get("/member/me")).andExpect(status().isOk());
    // then
    verify(memberService).getMyInformation(memberId);
  }

  @Test
  @DisplayName("내 정보 수정")
  @WithCustomMockUser
  void updateMember() throws Exception {
    // given
    UpdateMemberRequest request =
        new UpdateMemberRequest("위드콘", "010-1234-5678");

    CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    Long memberId = principal.getId();

    // when
    mockMvc.perform(patch("/member")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    // then
    verify(memberService).updateMember(memberId, request);
  }

  @Test
  @DisplayName("회원 탈퇴")
  @WithCustomMockUser
  void deleteMember() throws Exception {
    // given
    CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    Long memberId = principal.getId();

    // when
    mockMvc.perform(delete("/member"))
        .andExpect(status().isOk());

    // then
    verify(memberService).deleteMember(memberId);
  }
}