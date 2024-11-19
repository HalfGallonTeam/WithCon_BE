package com.halfgallon.withcon.domain.chat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import com.halfgallon.withcon.global.config.JpaAuditingConfig;
import com.halfgallon.withcon.global.config.QueryDslConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({JpaAuditingConfig.class, QueryDslConfig.class})
class ChatMessageRepositoryTest {

  @Autowired
  private ChatMessageRepository chatMessageRepository;

  @Test
  @DisplayName("No-Offset 방식을 사용하면 lastMsgId 값 -1부터 PageSize 만큼 가져온다.")
  void findChatRoomMessage() {
    //given
    Slice<ChatMessage> message
        = chatMessageRepository.findChatRoomMessage(10L, 1L, Pageable.ofSize(6));

    //when
    Long first = message.getContent().get(0).getId();
    Long last = message.getContent().get(5).getId();

    //then
    assertThat(first).isEqualTo(9);
    assertThat(last).isEqualTo(4);
  }

  @Test
  @DisplayName("마지막 페이지에서는 isLast가 true, 마지막이 아니면 isLast가 false")
  void findChatRoomMessage_checkLastPage() {
    //given
    Slice<ChatMessage> lastPage
        = chatMessageRepository.findChatRoomMessage(10L, 1L, Pageable.ofSize(9));

    Slice<ChatMessage> midPage
        = chatMessageRepository.findChatRoomMessage(10L, 1L, Pageable.ofSize(4));

    //when
    boolean isLastPage = lastPage.isLast();
    boolean isNotLastPage = midPage.isLast();

    //then
    assertThat(isLastPage).isTrue();
    assertThat(isNotLastPage).isFalse();
  }

}