package com.halfgallon.withcon.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponse implements Serializable {

  private Long notificationId;

  private Long memberId;

  private String message;

  private String url;

  @JsonProperty("createdAt")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  public NotificationResponse(Notification notification) {
    this.notificationId = notification.getId();
    this.memberId = notification.getMember().getId();
    this.message = notification.getMessage();
    this.url = notification.getUrl();
    this.createdAt = notification.getCreatedAt();
  }
}
