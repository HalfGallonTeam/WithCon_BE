package com.halfgallon.withcon.domain.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.member.dto.request.CurrentPasswordCheckRequest;
import com.halfgallon.withcon.domain.member.dto.request.UpdateMemberRequest;
import com.halfgallon.withcon.domain.member.service.MemberService;
import com.halfgallon.withcon.global.annotation.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

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
  @DisplayName("내 정보 수정 - 새 비밀번호가 null 일 때")
  @WithCustomMockUser
  void updateMember_when_newPassword_is_null() throws Exception {
    // given
    UpdateMemberRequest request =
        new UpdateMemberRequest("위드콘", "010-1234-5678", null);

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
  @DisplayName("내 정보 수정 - 새 비밀번호가 null이 아닐 때")
  @WithCustomMockUser
  void updateMember_when_newPassword_is_not_null() throws Exception {
    // given
    UpdateMemberRequest request =
        new UpdateMemberRequest("위드콘", "010-1234-5678", "newPassword1!");

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
  @DisplayName("현재 비밀번호 확인")
  @WithCustomMockUser
  void currentPasswordCheck() throws Exception {
    // given
    CurrentPasswordCheckRequest request =
        new CurrentPasswordCheckRequest("1q2w3e4r!");

    CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    Long memberId = principal.getId();

    // when
    mockMvc.perform(post("/member/current-password-check")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    // then
    verify(memberService).currentPasswordCheck(memberId, request.password());
  }

  @Test
  @DisplayName("회원 프로필 사진 업로드")
  @WithCustomMockUser
  void updateProfileImage() throws Exception {
    // given
    MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg",
        "테스트이미지 내용".getBytes());

    CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    Long memberId = principal.getId();

    doNothing().when(memberService).uploadProfileImage(any(Long.class), any(MultipartFile.class));

    // when
    mockMvc.perform(multipart("/member/profile-image").file(image))
        .andExpect(status().isOk());

    // then
    verify(memberService).uploadProfileImage(memberId, image);
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