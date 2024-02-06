package com.halfgallon.withcon.domain.chat.service.impl;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.chat.service.ChatParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatParticipantServiceImpl implements ChatParticipantService {

  private final ChatParticipantRepository chatParticipantRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<ChatParticipantResponse> findMyChatRoom(CustomUserDetails customUserDetails, Pageable pageable) {
    return chatParticipantRepository.findAllMyChattingRoom(customUserDetails.getId(), pageable)
        .map(ChatParticipantResponse::fromEntity);
  }

}
