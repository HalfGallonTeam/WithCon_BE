package com.halfgallon.withcon.domain.chat.repository.impl;

import static com.halfgallon.withcon.domain.chat.entity.QChatParticipant.chatParticipant;

import com.halfgallon.withcon.domain.chat.repository.CustomChatParticipantRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomChatParticipantRepositoryImpl implements CustomChatParticipantRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public boolean checkRoomManager(Long memberId) {
    Integer fetchOne = jpaQueryFactory.selectOne()
        .from(chatParticipant)
        .where(chatParticipant.member.id.eq(memberId),
            chatParticipant.isManager.eq(true))
        .fetchFirst();

    return fetchOne != null;
  }

}
