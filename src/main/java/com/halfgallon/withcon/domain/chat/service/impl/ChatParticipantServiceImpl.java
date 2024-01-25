package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.USER_NOT_PARTICIPANT_CHATTING;

import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.chat.service.ChatParticipantService;
import com.halfgallon.withcon.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatParticipantServiceImpl implements ChatParticipantService {

  private final ChatParticipantRepository chatParticipantRepository;

  @Override
  @Transactional(readOnly = true)
  public List<ChatParticipantResponse> findMyChatRoom(Long memberId) {
    if (!chatParticipantRepository.existsByMemberId(memberId)) {
      throw new CustomException(USER_NOT_PARTICIPANT_CHATTING);
    }

    return chatParticipantRepository.findAllByMemberId(memberId).stream()
        .map(ChatParticipantResponse::fromEntity).toList();
  }

}
