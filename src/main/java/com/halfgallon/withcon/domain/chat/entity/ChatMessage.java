package com.halfgallon.withcon.domain.chat.entity;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id")
  private ChatRoom room;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chatParticipant_id")
  private ChatParticipant chatParticipant;

  @Column(columnDefinition = "TEXT")
  private String message;

  @Enumerated(value = EnumType.STRING)
  private MessageType messageType;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime sendAt;

  public void updateChatRoom(ChatRoom chatRoom) {
    this.room = chatRoom;
  }

  public void updateChatParticipant(ChatParticipant chatParticipant) {
    this.chatParticipant = chatParticipant;
  }
}
