package com.halfgallon.withcon.domain.chat.repository.impl;

import static com.halfgallon.withcon.domain.chat.entity.QChatParticipant.chatParticipant;
import static com.halfgallon.withcon.domain.chat.entity.QChatRoom.chatRoom;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.repository.CustomChatParticipantRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class CustomChatParticipantRepositoryImpl implements CustomChatParticipantRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public boolean checkRoomManagerName(String managerName) {
    Integer i = jpaQueryFactory.selectOne()
        .from(chatParticipant)
        .leftJoin(chatParticipant.chatRoom, chatRoom)
        .where(chatRoom.managerName.eq(managerName))
        .fetchFirst();

    return i != null;
  }

  @Override
  public Page<ChatParticipant> findAllMyChattingRoom(Long memberId, Pageable pageable) {
    List<ChatParticipant> list = jpaQueryFactory.selectFrom(chatParticipant)
        .innerJoin(chatParticipant.chatRoom, chatRoom)
        .fetchJoin()
        .where(chatParticipant.member.id.eq(memberId))
        .orderBy(chatParticipant.chatRoom.userCount.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPAQuery<Long> countQuery = jpaQueryFactory.select(chatParticipant.count())
        .from(chatParticipant)
        .where(chatParticipant.member.id.eq(memberId));

    return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
  }
}
