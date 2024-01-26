package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.USER_NOT_PARTICIPANT_CHATTING;

import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.chat.service.ChatParticipantService;
import com.halfgallon.withcon.global.exception.CustomException;
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
  public Page<ChatParticipantResponse> findMyChatRoom(Long memberId, Pageable pageable) {
    if (!chatParticipantRepository.existsByMemberId(memberId)) {
      throw new CustomException(USER_NOT_PARTICIPANT_CHATTING);
    }

    return chatParticipantRepository.findAllByMemberId(memberId, pageable)
        .map(ChatParticipantResponse::fromEntity);
  }

}
