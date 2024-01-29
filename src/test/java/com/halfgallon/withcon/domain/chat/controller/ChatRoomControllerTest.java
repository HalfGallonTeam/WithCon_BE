package com.halfgallon.withcon.domain.chat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import com.halfgallon.withcon.domain.chat.service.impl.ChatRoomServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

  @MockBean
  ChatRoomServiceImpl chatRoomService;

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MockMvc mockMvc;
  @Autowired
  WebApplicationContext webApplicationContext;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .build();
  }

  @Test
  @DisplayName("채팅방 생성 완료")
  void createChatRoom_Success() throws Exception {
    //given
    given(chatRoomService.createChatRoom(any()))
        .willReturn(getChatRoomResponse());

    //when
    //then
    mockMvc.perform(post("/chatRoom")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new ChatRoomRequest(1L, "1번 채팅방"))))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  private static ChatRoomResponse getChatRoomResponse() {
    return ChatRoomResponse.builder()
        .chatRoomId(1L)
        .name("1번 채팅방")
        .build();
  }

  @Test
  @DisplayName("채팅방 목록 전체 조회 완료")
  void findChatRoom_Success() throws Exception {
    //given
    given(chatRoomService.findChatRoom(any()))
        .willReturn(new PageImpl<>(List.of(getChatRoomResponse())));

    //when
    //then
    mockMvc.perform(get("/chatRoom")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("채팅방 입장 완료")
  void enterChatRoom_success() throws Exception {
    //given
    given(chatRoomService.enterChatRoom(anyLong(), anyLong()))
        .willReturn(ChatRoomEnterResponse.builder()
            .chatRoomId(1L)
            .chatRoomName("1번채팅방")
            .build());

    //when
    //then
    mockMvc.perform(get("/chatRoom/{chatRoomId}/enter",1L)
            .param("memberId","1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.chatRoomId").value(1L))
        .andExpect(status().isOk())
        .andDo(print());
  }

}