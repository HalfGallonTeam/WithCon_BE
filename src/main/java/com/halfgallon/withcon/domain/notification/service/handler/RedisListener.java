package com.halfgallon.withcon.domain.notification.service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisListener implements MessageListener {

  private final ObjectMapper objectMapper;
  private final SseEmitterService sseEmitterService;

  @Autowired
  public RedisListener(ObjectMapper objectMapper, SseEmitterService sseEmitterService) {
    this.objectMapper = objectMapper;
    this.sseEmitterService = sseEmitterService;

    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      NotificationResponse notificationResponse =
          objectMapper.readValue(message.getBody(), NotificationResponse.class);
      log.info("매핑 데이터 역직렬화");

      sseEmitterService.sendNotificationToClient(notificationResponse);
      log.info("redis 메세지 전송");
    }catch (IOException e) {
      log.info("메세지 전송 실패" + e.getMessage());
    }
  }
}
