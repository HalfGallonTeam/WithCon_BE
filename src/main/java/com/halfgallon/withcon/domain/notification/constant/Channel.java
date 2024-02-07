package com.halfgallon.withcon.domain.notification.constant;

public class Channel {

  public static String makeChannel(Long performanceId, Long chatRoomId) {
    return performanceId + "-" + chatRoomId;
  }
}
