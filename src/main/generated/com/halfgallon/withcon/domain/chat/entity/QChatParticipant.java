package com.halfgallon.withcon.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatParticipant is a Querydsl query type for ChatParticipant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatParticipant extends EntityPathBase<ChatParticipant> {

    private static final long serialVersionUID = -1583382717L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatParticipant chatParticipant = new QChatParticipant("chatParticipant");

    public final QChatRoom chatRoom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isManager = createBoolean("isManager");

    public final com.halfgallon.withcon.domain.member.entity.QMember member;

    public QChatParticipant(String variable) {
        this(ChatParticipant.class, forVariable(variable), INITS);
    }

    public QChatParticipant(Path<? extends ChatParticipant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatParticipant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatParticipant(PathMetadata metadata, PathInits inits) {
        this(ChatParticipant.class, metadata, inits);
    }

    public QChatParticipant(Class<? extends ChatParticipant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new QChatRoom(forProperty("chatRoom")) : null;
        this.member = inits.isInitialized("member") ? new com.halfgallon.withcon.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

