package com.halfgallon.withcon.domain.chat.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.member.entity.Member;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@Disabled
@ExtendWith(MockitoExtension.class)
class ChatParticipantServiceImplTest {

  @InjectMocks
  ChatParticipantServiceImpl chatParticipantService;
  @Mock
  ChatParticipantRepository chatParticipantRepository;

  private Member member;
  private Pageable pageable;
  private CustomUserDetails customUserDetails;
  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .username("test1234")
        .phoneNumber("010-1234-5678")
        .password("12345")
        .build();

    customUserDetails = CustomUserDetails.fromEntity(member);

    pageable = PageRequest.of(0, 5, Sort.by(Direction.DESC, "id"));
  }


  @Test
  @DisplayName("나의 채팅방 조회 성공")
  void findMyChatRoom_Success() {
    //given
    given(chatParticipantRepository.findAllMyChattingRoom(1L, pageable))
        .willReturn(new PageImpl<>(List.of(ChatParticipant.builder()
            .id(1L)
            .chatRoom(ChatRoom.builder()
                .id(1L)
                .name("1번 채팅방")
                .build())
            .build())));

    //when
    Page<ChatParticipantResponse> responses
        = chatParticipantService.findMyChatRoom(customUserDetails, pageable);

    //then
    assertTrue(responses.hasContent());
  }

  @Test
  @DisplayName("나의 채팅방 조회 - 참여하고 있는 채팅방이 없습니다.")
  void findMyChatRoom_NotParticipant() {
    //given
    given(chatParticipantRepository.findAllMyChattingRoom(member.getId(), pageable))
        .willReturn(Page.empty());

    //when
    Page<ChatParticipantResponse> responses
        = chatParticipantService.findMyChatRoom(customUserDetails, pageable);

    //then
    assertThat(responses.hasContent()).isFalse();
  }
}