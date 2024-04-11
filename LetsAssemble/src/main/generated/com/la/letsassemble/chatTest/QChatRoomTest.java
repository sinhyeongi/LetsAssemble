package com.la.letsassemble.chatTest;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoomTest is a Querydsl query type for ChatRoomTest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoomTest extends EntityPathBase<ChatRoomTest> {

    private static final long serialVersionUID = -1870329839L;

    public static final QChatRoomTest chatRoomTest = new QChatRoomTest("chatRoomTest");

    public final NumberPath<Long> chatId = createNumber("chatId", Long.class);

    public final ListPath<com.la.letsassemble.Entity.Users, com.la.letsassemble.Entity.QUsers> memberList = this.<com.la.letsassemble.Entity.Users, com.la.letsassemble.Entity.QUsers>createList("memberList", com.la.letsassemble.Entity.Users.class, com.la.letsassemble.Entity.QUsers.class, PathInits.DIRECT2);

    public final StringPath roomName = createString("roomName");

    public final NumberPath<Long> userCount = createNumber("userCount", Long.class);

    public QChatRoomTest(String variable) {
        super(ChatRoomTest.class, forVariable(variable));
    }

    public QChatRoomTest(Path<? extends ChatRoomTest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRoomTest(PathMetadata metadata) {
        super(ChatRoomTest.class, metadata);
    }

}

