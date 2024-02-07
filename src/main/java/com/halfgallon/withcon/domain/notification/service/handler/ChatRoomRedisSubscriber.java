package com.halfgallon.withcon.domain.notification.service.handler;

import com.halfgallon.withcon.domain.notification.constant.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomRedisSubscriber {

  private final RedisMessageListenerContainer container;
  private final RedisListener subscriber;

  /**
   * 방장이 채팅방 생성시에 채팅방 ID로 redis 채널 생성
   */
  public void subscribeChatRoomChannel(Long performanceId, Long chatRoomId) {
    ChannelTopic topic = new ChannelTopic(
        Channel.makeChannel(performanceId, chatRoomId));
    container.addMessageListener(subscriber, topic);
    log.info("redis 채팅방 채널 생성");
  }

  /**
   * 공연이 삭제되거나 채팅방이 없어졌을 때 채널 해지
   */
  public void unSubscribeChatRoomChannel(Long performanceId, Long chatRoomId) {
    ChannelTopic topic = new ChannelTopic(
        Channel.makeChannel(performanceId, chatRoomId));
    container.removeMessageListener(subscriber, topic);
    log.info("redis 채팅방 채널 해지");
  }
}
