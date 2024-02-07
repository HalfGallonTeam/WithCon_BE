package com.halfgallon.withcon.domain.chat.entity;

import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.tag.entity.Tag;
import com.halfgallon.withcon.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Builder.Default
  private int userCount = 1;

  @Builder.Default
  @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
  List<ChatParticipant> chatParticipants = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Tag> tags = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "performance_id")
  Performance performance;

  public void updateUserCount() {
    this.userCount = this.chatParticipants.size();
  }

  public void addChatParticipant(ChatParticipant chatParticipant) {
    this.chatParticipants.add(chatParticipant);
    this.updateUserCount();
  }

  public void removeChatParticipant(ChatParticipant chatParticipant) {
    this.chatParticipants.remove(chatParticipant);
    this.updateUserCount();
  }

  public void addTag(Tag tag) {
    this.tags.add(tag);
  }
}
