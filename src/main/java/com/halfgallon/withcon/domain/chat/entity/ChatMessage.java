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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  @JoinColumn(name = "chatRoom_id")
  private ChatRoom chatRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chatParticipant_id")
  private ChatParticipant chatParticipant;

  @Column(columnDefinition = "TEXT")
  private String message;

  @Enumerated(value = EnumType.STRING)
  private MessageType messageType;

  @Column(nullable = false)
  private Long sendAt;

  public void updateChatRoom(ChatRoom chatRoom) {
    this.chatRoom = chatRoom;
  }

  public void updateChatParticipant(ChatParticipant chatParticipant) {
    this.chatParticipant = chatParticipant;
  }
}
