package com.halfgallon.withcon.domain.notification.entity;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private boolean isRead;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
