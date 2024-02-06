package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.USER_NOT_PARTICIPANT_CHATTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.global.exception.CustomException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class ChatParticipantServiceImplTest {

  @InjectMocks
  ChatParticipantServiceImpl chatParticipantService;

  @Mock
  ChatParticipantRepository chatParticipantRepository;


  @Test
  @DisplayName("나의 채팅방 조회 실패 - 참여하는 채팅이 없음")
  void findMyChatRoom_FailByExistsMember() {
    //given
    given(chatParticipantRepository.existsByMemberId(anyLong()))
        .willReturn(false);

    Pageable pageable = PageRequest.of(0, 5, Sort.by(Direction.DESC, "id"));

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> chatParticipantService.findMyChatRoom(1L, pageable));

    //then
    assertThat(USER_NOT_PARTICIPANT_CHATTING.getDescription()).isEqualTo(customException.getMessage());
  }

  @Test
  @DisplayName("나의 채팅방 조회 성공")
  void findMyChatRoom_Success() {
    //given
    Pageable pageable = PageRequest.of(0, 5, Sort.by(Direction.DESC, "id"));

    given(chatParticipantRepository.existsByMemberId(anyLong()))
        .willReturn(true);

    given(chatParticipantRepository.findAllMyChattingRoom(1L, pageable))
        .willReturn(new PageImpl<>(List.of(ChatParticipant.builder()
            .id(1L)
            .chatRoom(ChatRoom.builder()
                .id(1L)
                .name("1번 채팅방")
                .build())
            .member(Member.builder()
                .id(1L)
                .username("test1234")
                .phoneNumber("010-1234-5678")
                .password("12345")
                .build())
            .build())));

    //when
    Page<ChatParticipantResponse> responses
        = chatParticipantService.findMyChatRoom(1L, pageable);

    //then
    assertTrue(responses.hasContent());
  }
}