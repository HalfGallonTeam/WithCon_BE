package com.halfgallon.withcon.domain.chat.repository.impl;

import static com.halfgallon.withcon.domain.chat.entity.QChatMessage.chatMessage;
import static com.halfgallon.withcon.domain.chat.entity.QChatParticipant.chatParticipant;

import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import com.halfgallon.withcon.domain.chat.repository.CustomChatMessageRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.ObjectUtils;

@Slf4j
@RequiredArgsConstructor
public class CustomChatMessageRepositoryImpl implements CustomChatMessageRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Slice<ChatMessage> findChatRoomMessage(Long messageId, Long roomId, Pageable pageable) {
    List<ChatMessage> messages = jpaQueryFactory.selectFrom(chatMessage)
        .join(chatMessage.chatParticipant, chatParticipant)
        .fetchJoin()
        .where(
            ltMessageId(messageId), //no-offset 페이징 처리
            chatMessage.room.id.eq(roomId)
        )
        .limit(pageable.getPageSize() + 1)  //마지막 페이지 확인을 위해서 +1 조회
        .orderBy(chatMessage.id.desc())
        .fetch();

    return new SliceImpl<>(messages, pageable, checkLastPage(messages, pageable));
  }

  private BooleanExpression ltMessageId(Long messageId) {
    return ObjectUtils.isEmpty(messageId) ? null : chatMessage.id.lt(messageId);
  }

  //무한 스크롤 방식을 처리하는 메서드
  private boolean checkLastPage(List<ChatMessage> messages, Pageable pageable) {

    //조회한 결과 갯수가 요청한 페이지 사이즈보다 크면 이외의 데이터가 존재한다. => true
    if (messages.size() > pageable.getPageSize()) {
      messages.remove(pageable.getPageSize()); //확인하기 위해 추가한 데이터 (limit + 1) remove
      return true;
    }
    return false;
  }

}
