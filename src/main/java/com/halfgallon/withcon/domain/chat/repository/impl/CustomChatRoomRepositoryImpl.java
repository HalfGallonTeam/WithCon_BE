package com.halfgallon.withcon.domain.chat.repository.impl;

import static com.halfgallon.withcon.domain.chat.entity.QChatRoom.chatRoom;
import static com.halfgallon.withcon.domain.tag.entity.QTag.tag;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.chat.repository.CustomChatRoomRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class CustomChatRoomRepositoryImpl implements CustomChatRoomRepository {

  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public Page<ChatRoom> findAllTagNameChatRoom(String performanceId, String tagName,
      Pageable pageable) {

    List<ChatRoom> fetch = jpaQueryFactory.selectFrom(chatRoom)
        .leftJoin(chatRoom.tags, tag)
        .where(
            chatRoom.performance.id.eq(performanceId),
            tag.name.startsWithIgnoreCase(tagName)
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();


    JPAQuery<Long> countQuery = jpaQueryFactory.select(chatRoom.count())
        .from(chatRoom)
        .leftJoin(chatRoom.tags, tag)
        .where(chatRoom.performance.id.eq(performanceId), tag.name.startsWithIgnoreCase(tagName));

    return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
  }
}
